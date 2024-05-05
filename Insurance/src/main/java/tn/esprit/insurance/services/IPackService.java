package tn.esprit.insurance.services;

import tn.esprit.insurance.entities.Pack;

import java.util.List;

public interface IPackService {
    Pack addPack(Pack ins);

    Pack updatePack(Pack Pack);

    void deletePack(int id);

    List<Pack> getAll();

    Pack getPackById(int id);
}
