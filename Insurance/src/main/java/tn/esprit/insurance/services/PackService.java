package tn.esprit.insurance.services;

import org.springframework.stereotype.Service;
import tn.esprit.insurance.entities.Pack;
import tn.esprit.insurance.repositories.IPackRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PackService implements IPackService{
    IPackRepository pr;

    public Pack addPack(Pack p) {
        return (Pack)this.pr.save(p);
    }

    public Pack updatePack(Pack p) {
        return (Pack)this.pr.save(p);
    }

    public void deletePack(int id) {
        this.pr.deleteById(id);
    }

    public List<Pack> getAll() {
        return (List<Pack>)this.pr.findAll();
    }

    public Pack getPackById(int id) {
        return (Pack)this.pr.findById(id).orElse(null);
    }
    public void all(){
        List<Pack> packList = new ArrayList<>();
        packList.add(new Pack("Education", "Complete educational support", 20000, 500.0f, 20,
                true, false, true, false, true, "Education Pack"));
        packList.add(new Pack("Homeownership", "Assistance in buying a house", 50000, 1000, 15,
                false, true, false, true, false, "Homeownership Pack"));
        packList.add(new Pack("Entrepreneurship", "Resources for starting a business", 10000, 1000, 18,
                true, false, true, true, true, "Entrepreneurship Pack"));
        packList.add(new Pack("Healthcare", "Health insurance and medical aid", 15000, 8000, 30,
                true, false, false, false, true, "Healthcare Pack"));
        packList.add(new Pack("Travel", "Travel discounts and benefits", 10000, 5000, 40,
                false, false, false, true, true, "Travel Pack"));
        packList.add(new Pack("Financial", "Financial planning and investment advice", 18000, 90000, 17,
                true, true, false, false, true, "Financial Pack"));
        this.pr.saveAll(packList);
    }

    public PackService(final IPackRepository pr) {
        this.pr = pr;
    }
}
