package com.omnia.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador raíz de la API.
 * Proporciona una página de bienvenida con enlaces a la documentación interactiva.
 */
@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8"/>
              <title>Omnia API</title>
              <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                body {
                  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
                  background: #0D0B2A;
                  color: #E2E8F0;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                  min-height: 100vh;
                }
                .card {
                  background: rgba(255,255,255,0.04);
                  border: 1px solid rgba(255,255,255,0.08);
                  border-radius: 20px;
                  padding: 48px 56px;
                  max-width: 520px;
                  width: 90%;
                  text-align: center;
                }
                .badge {
                  display: inline-block;
                  background: rgba(79, 70, 229, 0.2);
                  color: #A5B4FC;
                  font-size: 12px;
                  font-weight: 600;
                  letter-spacing: 0.1em;
                  text-transform: uppercase;
                  padding: 4px 14px;
                  border-radius: 99px;
                  border: 1px solid rgba(79, 70, 229, 0.3);
                  margin-bottom: 20px;
                }
                h1 {
                  font-size: 2rem;
                  font-weight: 800;
                  color: #F8FAFC;
                  letter-spacing: -0.03em;
                  margin-bottom: 10px;
                }
                .subtitle {
                  color: #94A3B8;
                  font-size: 0.95rem;
                  margin-bottom: 36px;
                  line-height: 1.6;
                }
                .links {
                  display: flex;
                  flex-direction: column;
                  gap: 12px;
                }
                a {
                  display: flex;
                  align-items: center;
                  justify-content: space-between;
                  padding: 14px 20px;
                  border-radius: 12px;
                  text-decoration: none;
                  font-size: 0.9rem;
                  font-weight: 500;
                  transition: all 0.2s;
                }
                .primary-link {
                  background: #312E81;
                  color: #E0E7FF;
                }
                .primary-link:hover { background: #4F46E5; transform: translateY(-1px); }
                .secondary-link {
                  background: rgba(255,255,255,0.04);
                  color: #94A3B8;
                  border: 1px solid rgba(255,255,255,0.07);
                }
                .secondary-link:hover { background: rgba(255,255,255,0.08); color: #E2E8F0; }
                .arrow { font-size: 1rem; opacity: 0.6; }
                .footer {
                  margin-top: 32px;
                  font-size: 0.75rem;
                  color: #475569;
                }
              </style>
            </head>
            <body>
              <div class="card">
                <div class="badge">REST API · v1.0</div>
                <h1>Omnia Manager</h1>
                <p class="subtitle">
                  API de gestión empresarial. Todos los endpoints están
                  documentados e disponibles para explorar de forma interactiva.
                </p>
                <div class="links">
                  <a href="/swagger-ui/index.html" class="primary-link">
                    <span>📖 Documentación interactiva (Swagger)</span>
                    <span class="arrow">→</span>
                  </a>
                </div>
                <p class="footer">Omnia S.L. © 2026 · Solo para uso interno</p>
              </div>
            </body>
            </html>
            """;
    }
}
