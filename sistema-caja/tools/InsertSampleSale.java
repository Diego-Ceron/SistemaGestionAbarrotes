import java.util.Date;

import com.tienda.producto.ProductoModel;
import com.tienda.venta.VentaDAODB;
import com.tienda.venta.VentaModel;

public class InsertSampleSale {
    public static void main(String[] args) {
        VentaDAODB dao = new VentaDAODB();
        ProductoModel p = new ProductoModel();
        p.setId(1); // as seen earlier, product with id 1 exists
        p.setNombre("Coca Cola");
        p.setPrecio(10.0);
        p.setCantidad(2);

        VentaModel v = new VentaModel();
        v.setCliente("Prueba");
        v.setFecha(new Date());
        v.setProductos(new ProductoModel[]{p});
        v.setTotal(p.getPrecio() * p.getCantidad());

        dao.registrarVenta(v);
        System.out.println("Venta insertada con ID (posible 0 si no se generó): " + v.getId());
    }
}
