package tn.esprit.insurance.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import tn.esprit.insurance.entities.User;
import tn.esprit.insurance.repositories.IUserazerRepository;
@Service
public class UserazerService {
   // HttpSession session ;
    IUserazerRepository ur;

    public UserazerService(IUserazerRepository ur) {
        this.ur = ur;
    }
    public User getUserById(int id) {
        return (User)this.ur.findById(id).orElse((User)null);
    }
    public User getusersession(HttpSession session){


        User u = (User) session.getAttribute("User");
        return u;
    }
}
