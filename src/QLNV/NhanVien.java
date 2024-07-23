package QLNV;

public class NhanVien {
    private int id;
    private String hoTen;
    private String namSinh;
    private String diaChi;
    private String sdt;
    private String chucVu;

    public NhanVien(String hoTen, String namSinh, String diaChi, String sdt, String chucVu) {
        this.hoTen = hoTen;
        this.namSinh = namSinh;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.chucVu = chucVu;
    }

    public NhanVien() {
        this.id = 0;
    }

    public NhanVien(int id, String hoTen, String namSinh, String diaChi, String sdt, String chucVu) {
        this.id = id;
        this.hoTen = hoTen;
        this.namSinh = namSinh;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.chucVu = chucVu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(String namSinh) {
        this.namSinh = namSinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", hoTen='" + hoTen + '\'' +
                ", namSinh='" + namSinh + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", sdt='" + sdt + '\'' +
                ", chucVu='" + chucVu + '\'' +
                '}';
    }
}
