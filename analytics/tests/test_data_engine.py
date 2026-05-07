import unittest
import pandas as pd
from data_engine import DataEngine

class TestDataEngine(unittest.TestCase):
    """
    Suite de pruebas unitarias para el motor de datos de analítica.
    Usa la librería estándar unittest para máxima compatibilidad.
    """

    def setUp(self):
        # Forzamos modo demo para pruebas unitarias
        self.engine = DataEngine()

    def test_get_employees_structure(self):
        """Verifica que el DataFrame de empleados tenga las columnas esperadas."""
        df = self.engine.get_employees()
        self.assertIsInstance(df, pd.DataFrame)
        self.assertFalse(df.empty)
        self.assertIn("ANTIGUEDAD_ANOS", df.columns)
        self.assertIn("EDAD", df.columns)
        self.assertIn("ESTADO", df.columns)

    def test_enrich_employees_logic(self):
        """Valida la lógica de cálculo de antigüedad y edad."""
        data = {
            "F_ALTA": ["2020-01-01"],
            "F_NACIMIENTO": ["1990-01-01"],
            "F_BAJA": [None]
        }
        df = pd.DataFrame(data)
        enriched = self.engine._enrich_employees(df)
        
        self.assertGreater(enriched["ANTIGUEDAD_ANOS"].iloc[0], 4)
        self.assertTrue(30 < enriched["EDAD"].iloc[0] < 40)
        self.assertEqual(enriched["ESTADO"].iloc[0], "Activo")

    def test_get_projects_duracion(self):
        """Verifica que el cálculo de duración de proyectos no sea negativo."""
        df = self.engine.get_projects()
        self.assertTrue((df["DURACION_DIAS"] >= 0).all())
        self.assertIn("ESTADO", df.columns)

    def test_get_assignments_non_empty(self):
        """Asegura que hay asignaciones cargadas para el análisis."""
        df = self.engine.get_assignments()
        self.assertFalse(df.empty)
        cols = ["PROYECTO", "TX_NOMBRE", "ID_EMPLEADO", "ID_PROYECTO"]
        for col in cols:
            self.assertIn(col, df.columns)

if __name__ == '__main__':
    unittest.main()
