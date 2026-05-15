package com.tutorbooking.controller;

import com.tutorbooking.model.Admin;
import com.tutorbooking.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService = new AdminService();

    // Admin Login Pages
    @GetMapping("/login")
    public String showAdminLoginForm() {
        return "admin/admin-login";
    }

    @PostMapping("/login")
    public String processAdminLogin(@RequestParam String email, @RequestParam String password,
                                    HttpSession session, Model model) {
        Admin admin = adminService.authenticateAdmin(email, password);
        if (admin != null) {
            session.setAttribute("adminUser", admin);
            session.setAttribute("isAdmin", true);
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid admin credentials");
        return "admin/admin-login";
    }

    @GetMapping("/logout")
    public String adminLogout(HttpSession session) {
        session.removeAttribute("adminUser");
        session.removeAttribute("isAdmin");
        return "redirect:/admin/login";
    }

    // Admin Dashboard
    @GetMapping("/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        model.addAttribute("admin", admin);
        model.addAttribute("totalUsers", adminService.getTotalUsers());
        model.addAttribute("totalTutors", adminService.getTotalTutors());
        model.addAttribute("totalBookings", adminService.getTotalBookings());
        model.addAttribute("totalReviews", adminService.getTotalReviews());
        model.addAttribute("totalRevenue", adminService.getTotalRevenue());
        model.addAttribute("averageRating", adminService.getAverageRating());
        model.addAttribute("completedBookings", adminService.getCompletedBookings());
        model.addAttribute("pendingBookings", adminService.getPendingBookings());

        return "admin/admin-dashboard";
    }

    // View All Users
    @GetMapping("/users")
    public String viewAllUsers(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("admin", admin);
        return "admin/admin-users";
    }

    // View All Tutors
    @GetMapping("/tutors")
    public String viewAllTutors(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        model.addAttribute("tutors", adminService.getAllTutors());
        model.addAttribute("admin", admin);
        return "admin/admin-tutor";
    }

    // View All Bookings
    @GetMapping("/bookings")
    public String viewAllBookings(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        model.addAttribute("bookings", adminService.getAllBookings());
        model.addAttribute("admin", admin);
        return "admin/admin-bookings";
    }

    // View All Payments
    @GetMapping("/payments")
    public String viewAllPayments(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        model.addAttribute("payments", adminService.getAllPayments());
        model.addAttribute("admin", admin);
        return "admin/admin-payments";
    }

    // View All Reviews
    @GetMapping("/reviews")
    public String viewAllReviews(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        model.addAttribute("reviews", adminService.getAllReviews());
        model.addAttribute("admin", admin);
        return "admin/admin-reviews";
    }

    // Delete Operations
    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable String id, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        adminService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete-tutor/{id}")
    public String deleteTutor(@PathVariable String id, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        adminService.deleteTutor(id);
        return "redirect:/admin/tutors";
    }

    @GetMapping("/delete-booking/{id}")
    public String deleteBooking(@PathVariable String id, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        adminService.deleteBooking(id);
        return "redirect:/admin/bookings";
    }

    @GetMapping("/delete-payment/{id}")
    public String deletePayment(@PathVariable String id, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        adminService.deletePayment(id);
        return "redirect:/admin/payments";
    }

    @GetMapping("/delete-review/{id}")
    public String deleteReview(@PathVariable String id, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        adminService.deleteReview(id);
        return "redirect:/admin/reviews";
    }

    // Admin Management - View All Admins
    @GetMapping("/admins")
    public String viewAllAdmins(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        model.addAttribute("admins", adminService.getAllAdmins());
        model.addAttribute("admin", admin);
        return "admin/admin-list";
    }

    // Admin Management - Register New Admin
    @GetMapping("/admin/register")
    public String showAdminRegisterForm(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        model.addAttribute("admin", admin);
        return "admin/admin-register";
    }

    @PostMapping("/admin/register")
    public String registerNewAdmin(@RequestParam String name, @RequestParam String email,
                                   @RequestParam String password, @RequestParam String adminLevel,
                                   HttpSession session, Model model) {
        Admin currentAdmin = (Admin) session.getAttribute("adminUser");
        if (currentAdmin == null)
            return "redirect:/admin/login";

        // Check if email already exists
        if (adminService.getAdminByEmail(email) != null) {
            model.addAttribute("error", "Email already registered as admin");
            model.addAttribute("admin", currentAdmin);
            return "admin/admin-register";
        }

        // Generate new admin ID
        String adminId = "A" + (adminService.getAllAdmins().size() + 1);
        String createdDate = java.time.LocalDate.now().toString();

        Admin newAdmin = new Admin(adminId, name, email, password, adminLevel, createdDate);
        adminService.createAdmin(newAdmin);

        model.addAttribute("success", "New admin registered successfully");
        model.addAttribute("admin", currentAdmin);
        return "admin/admin-register";
    }

    // Admin Management - Edit Admin
    @GetMapping("/admin/edit/{id}")
    public String showEditAdminForm(@PathVariable String id, HttpSession session, Model model) {
        Admin currentAdmin = (Admin) session.getAttribute("adminUser");
        if (currentAdmin == null)
            return "redirect:/admin/login";

        Admin adminToEdit = adminService.getAdminById(id);
        if (adminToEdit == null)
            return "redirect:/admin/admins";

        model.addAttribute("admin", currentAdmin);
        model.addAttribute("adminToEdit", adminToEdit);
        return "admin/admin-edit";
    }

    @PostMapping("/admin/update/{id}")
    public String updateAdmin(@PathVariable String id, @RequestParam String name,
                              @RequestParam String email, @RequestParam String adminLevel,
                              HttpSession session) {
        Admin currentAdmin = (Admin) session.getAttribute("adminUser");
        if (currentAdmin == null)
            return "redirect:/admin/login";

        Admin adminToUpdate = adminService.getAdminById(id);
        if (adminToUpdate == null)
            return "redirect:/admin/admins";

        adminToUpdate.setName(name);
        adminToUpdate.setEmail(email);
        adminToUpdate.setAdminLevel(adminLevel);
        adminService.updateAdmin(id, adminToUpdate);

        return "redirect:/admin/admins";
    }

    // Admin Management - Delete Admin
    @GetMapping("/delete-admin/{id}")
    public String deleteAdmin(@PathVariable String id, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("adminUser");
        if (admin == null)
            return "redirect:/admin/login";

        adminService.deleteAdmin(id);
        return "redirect:/admin/admins";
    }
}
