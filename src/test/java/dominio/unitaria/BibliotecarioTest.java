package dominio.unitaria;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dominio.WrapperDate;
import dominio.excepcion.PrestamoException;
import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import testdatabuilder.LibroTestDataBuilder;

import java.util.Calendar;
import java.util.Date;


public class BibliotecarioTest {

    private static final String MARCOS = "Marcos";

    @Test
    public void esPrestadoTest() {

        // arrange
        LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder();

        Libro libro = libroTestDataBuilder.build();

        RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
        RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

        when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(libro);

        Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

        // act
        try {
            bibliotecario.prestar(libro.getIsbn(), MARCOS);
            fail();
        } catch (PrestamoException e) {

            //assert
            assertEquals(Bibliotecario.EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE, e.getMessage());
        }

    }

    @Test
    public void esPalindromoTest() {

        // arrange
        Libro libro = new LibroTestDataBuilder().conIsbn("1221").build();

        RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
        RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

        when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(null);

        Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

        try {
            // act
            bibliotecario.prestar(libro.getIsbn(), MARCOS);
            fail();
        } catch (PrestamoException e) {
            // assert
            assertEquals(Bibliotecario.NO_SE_PRESTAN_LIBROS_PALINDROMOS, e.getMessage());
        }
    }

    @Test
    public void esMayorTest() {
        // arrange
        Libro libro = new LibroTestDataBuilder().conIsbn("8g9f7t9").build();

        RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
        RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

        when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn()))
                .thenReturn(null);

        Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

        // act
        bibliotecario.prestar(libro.getIsbn(), MARCOS);

        // asserts

        assertNotNull(repositorioPrestamo.obtener(libro.getIsbn()).getFechaEntregaMaxima());
    }

    @Test
    public void noEsMayorTest() {
        // arrange
        Libro libro = new LibroTestDataBuilder().conIsbn("6g1f3t2").build();

        RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
        RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

        when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(null);

        Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

        // act
        bibliotecario.prestar(libro.getIsbn(), MARCOS);

        // asserts
        assertNull(repositorioPrestamo.obtener(libro.getIsbn()).getFechaEntregaMaxima());


    }

    @Test
    public void esMayorA30YCaeDomingoLaEntregaTest(){
        // arrange
        Libro libro = new LibroTestDataBuilder().conIsbn("8g9f7t9").build();

        Date dateMax=new Date(2019,11 , 11);;

        RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
        RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
        WrapperDate wrapperDate = mock(WrapperDate.class);

        when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn()))
                .thenReturn(null);

        when(wrapperDate.nuevaFecha()).thenReturn(new Date(2019, 10, 25));

        Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

        // act
        bibliotecario.prestar(libro.getIsbn(), MARCOS);

        // assert
        assertEquals(repositorioPrestamo.obtener(libro.getIsbn()).getFechaEntregaMaxima(),dateMax);

    }



}
