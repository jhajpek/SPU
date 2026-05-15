package hr.fer.zpr.infsus.spu.util;

import hr.fer.zpr.infsus.spu.model.Administrator;

public class EntityFactory {

    public static Administrator createAdministrator() {
        Administrator administrator = new Administrator();
        administrator.setIme("Admin");
        administrator.setPrezime("Adminović");
        administrator.setEmail("admin@gmail.com");
        administrator.setLozinka("$2a$12$0EtFHHKP7awf6Jd0zZraMu8ZRhXpan63DVisMvvr/WmnHoCm9T1Dm");
        return administrator;
    }

}
