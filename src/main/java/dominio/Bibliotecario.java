package dominio;

import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class Bibliotecario {

    public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";
    public static final String NO_SE_PRESTAN_LIBROS_PALINDROMOS = "Los libros políndromos solo se pueden utilizar en la biblitoeca";

    private RepositorioLibro repositorioLibro;
    private RepositorioPrestamo repositorioPrestamo;

    public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
        this.repositorioLibro = repositorioLibro;
        this.repositorioPrestamo = repositorioPrestamo;

    }

    public void prestar(String isbn, String nombreUsuario) {
        if (esPrestado(isbn)) {
            throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
        }

        if (esPalindromo(isbn)) {
            throw new PrestamoException(NO_SE_PRESTAN_LIBROS_PALINDROMOS);
        }
        Date fechaSolicitud = new Date();
        Date fechaEntregaMaxima = null;

        if (esMayor(isbn)) {
            fechaEntregaMaxima = calcularFechaEntregaMaxima(fechaSolicitud);
        }

        Libro libro = this.repositorioLibro.obtenerPorIsbn(isbn);
        Prestamo prestamo = new Prestamo(fechaSolicitud, libro, fechaEntregaMaxima, nombreUsuario);
        this.repositorioPrestamo.agregar(prestamo);
    }

    private boolean esPalindromo(String isbn) {
        String invertido = new StringBuilder().reverse().toString();
        return invertido.equals(isbn);
    }

    private boolean esPrestado(String isbn) {
        Libro libro = this.repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn);
        return Objects.nonNull(libro);
    }

    private boolean esMayor(String isbn) {
        char[] cadena = isbn.toCharArray();
        int suma = 0;

        for (char c : cadena) {
            if (Character.isDigit(c)) {
                suma += Integer.parseInt("" + c);
            }
        }

        return suma > 30;
    }

    private Date calcularFechaEntregaMaxima(Date fechaSolicitud) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaSolicitud);
        int dias = 15;

        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            dias = dias - 1;
        }

        while (dias > 0) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                dias--;
            }
        }

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return calendar.getTime();
    }


}
