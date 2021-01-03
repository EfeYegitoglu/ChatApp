package com.efeyegitoglu.sohbet_uygulamasi;

public class KullaniciBilgiler {

    private String dogumtarihi,egitim,hakkimda,isim,resim;

    public KullaniciBilgiler(String dogumtarihi, String egitim, String hakkimda, String isim, String resim) {
        this.dogumtarihi = dogumtarihi;
        this.egitim = egitim;
        this.hakkimda = hakkimda;
        this.isim = isim;
        this.resim = resim;

    }

    public KullaniciBilgiler() {
    }

    public String getDogumtarihi() {
        return dogumtarihi;
    }

    public void setDogumtarihi(String dogumtarihi) {
        this.dogumtarihi = dogumtarihi;
    }

    public String getEgitim() {
        return egitim;
    }

    public void setEgitim(String egitim) {
        this.egitim = egitim;
    }

    public String getHakkimda() {
        return hakkimda;
    }

    public void setHakkimda(String hakkimda) {
        this.hakkimda = hakkimda;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    @Override
    public String toString() {
        return "KullaniciBilgiler{" +
                "dogumtarihi='" + dogumtarihi + '\'' +
                ", egitim='" + egitim + '\'' +
                ", hakkimda='" + hakkimda + '\'' +
                ", isim='" + isim + '\'' +
                ", resim='" + resim + '\'' +
                '}';
    }


}
