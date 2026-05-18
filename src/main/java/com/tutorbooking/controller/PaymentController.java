package com.tutorbooking.controller;

import com.tutorbooking.model.Payment;
import com.tutorbooking.model.Card;
import com.tutorbooking.model.User;
import com.tutorbooking.model.Booking;
import com.tutorbooking.service.PaymentService;
import com.tutorbooking.service.CardService;
import com.tutorbooking.service.BookingService;
import com.tutorbooking.service.UserService;
import com.tutorbooking.util.IDGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService = new PaymentService();
    private final CardService cardService = new CardService();
    private final BookingService bookingService = new BookingService();
    private final UserService userService;

    public PaymentController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/history")
    public String viewHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        model.addAttribute("payments", paymentService.getPaymentsByUserId(user.getId()));
        return "payment/history";
    }

    @GetMapping("/create")
    public String showPaymentForm(@RequestParam(required = false) String bookingId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        if (bookingId != null && !bookingId.isEmpty()) {
            Booking booking = bookingService.getBookingById(bookingId);
            if (booking != null) {
                model.addAttribute("booking", booking);
                model.addAttribute("bookingId", bookingId);
                model.addAttribute("studentId", user.getId());
            } else {
                model.addAttribute("error", "Booking not found. Please select a valid booking.");
                return "redirect:/bookings/list";
            }
        } else {
            model.addAttribute("error", "Please select a booking to make payment.");
            return "redirect:/bookings/list";
        }

        model.addAttribute("userCards", cardService.getCardsByUserId(user.getId()));
        return "payment/make-payment";
    }

    @PostMapping("/process")
    public String makePayment(@RequestParam String bookingId, @RequestParam String studentId,
                              @RequestParam Double amount, @RequestParam String paymentMethod,
                              @RequestParam(required = false) String cardId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getId().equals(studentId)) {
            return "redirect:/login";
        }

        // Validate booking exists
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            model.addAttribute("error", "Booking not found");
            return "redirect:/bookings/list";
        }

        // Validate payment method
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            model.addAttribute("error", "Please select a payment method");
            model.addAttribute("booking", booking);
            model.addAttribute("bookingId", bookingId);
            model.addAttribute("studentId", studentId);
            model.addAttribute("userCards", cardService.getCardsByUserId(studentId));
            return "payment/make-payment";
        }

        // Validate amount
        if (amount == null || amount <= 0) {
            model.addAttribute("error", "Please enter a valid amount");
            model.addAttribute("booking", booking);
            model.addAttribute("bookingId", bookingId);
            model.addAttribute("studentId", studentId);
            model.addAttribute("userCards", cardService.getCardsByUserId(studentId));
            return "payment/make-payment";
        }

        if ("CARD".equals(paymentMethod) && cardId != null && !cardId.isEmpty()) {
            if (!cardService.cardBelongsToUser(cardId, studentId)) {
                model.addAttribute("error", "Invalid card selected");
                model.addAttribute("booking", booking);
                model.addAttribute("bookingId", bookingId);
                model.addAttribute("studentId", studentId);
                model.addAttribute("userCards", cardService.getCardsByUserId(studentId));
                return "payment/make-payment";
            }
        }

        String id = IDGenerator.generate("P");
        String paymentDate = LocalDate.now().toString();
        Payment payment = new Payment(id, bookingId, studentId, amount, paymentMethod, paymentDate, "PAID");
        paymentService.processPayment(payment);
        return "redirect:/payments/history";
    }

    // Card Management Endpoints
    @GetMapping("/cards")
    public String viewCards(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        model.addAttribute("cards", cardService.getCardsByUserId(user.getId()));
        return "payment/manage-cards";
    }

    @GetMapping("/cards/add")
    public String showAddCardForm(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        return "payment/add-card";
    }

    @PostMapping("/cards/save")
    public String addCard(@RequestParam String cardholderName, @RequestParam String cardNumber,
                          @RequestParam String expiryDate, @RequestParam String cvv,
                          HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        try {
            Card card = new Card(null, user.getId(), cardholderName, cardNumber, expiryDate, cvv);
            cardService.addCard(card);
            return "redirect:/payments/cards";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "payment/add-card";
        }
    }

    @GetMapping("/cards/edit/{cardId}")
    public String showEditCardForm(@PathVariable String cardId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        Card card = cardService.getCardById(cardId);
        if (card == null || !card.getUserId().equals(user.getId())) {
            return "redirect:/payments/cards";
        }

        model.addAttribute("card", card);
        return "payment/edit-card";
    }

    @PostMapping("/cards/update/{cardId}")
    public String updateCard(@PathVariable String cardId, @RequestParam String cardholderName,
                             @RequestParam String cardNumber, @RequestParam String expiryDate,
                             @RequestParam String cvv, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        Card card = cardService.getCardById(cardId);
        if (card == null || !card.getUserId().equals(user.getId())) {
            return "redirect:/payments/cards";
        }

        try {
            cardService.updateCard(cardId, cardholderName, cardNumber, expiryDate, cvv);
            return "redirect:/payments/cards";
        } catch (IllegalArgumentException e) {
            model.addAttribute("card", card);
            model.addAttribute("error", e.getMessage());
            return "payment/edit-card";
        }
    }

    @GetMapping("/cards/delete/{cardId}")
    public String deleteCard(@PathVariable String cardId, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/login";

        Card card = cardService.getCardById(cardId);
        if (card != null && card.getUserId().equals(user.getId())) {
            cardService.deleteCard(cardId);
        }

        return "redirect:/payments/cards";
    }
}
