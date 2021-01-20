package tds.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import tds.controlador.ControladorVistaModeloTest;
import tds.modelo.CatalogoCancionesTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({ ControladorVistaModeloTest.class, CatalogoCancionesTest.class })

public class TestSuite {

}
