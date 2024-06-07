package tn.iit.web;

import java.io.*;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tn.iit.dao.DemandeDao;
import tn.iit.model.demande;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class UploadServlet extends HttpServlet {
    private DemandeDao demandeDao;
    public void init() {
        demandeDao = new DemandeDao();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer le contenu du champ "demande"
        String demande = request.getParameter("demande");

        // Récupérer le contenu du fichier uploadé
        Part filePart = request.getPart("file");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        InputStream fileContent = filePart.getInputStream();

        // Répertoire de destination pour sauvegarder le fichier
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Chemin complet du fichier uploadé
        String filePath = uploadPath + File.separator + fileName;
        File file = new File(filePath);

        // Sauvegarder le fichier uploadé sur le serveur
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = fileContent.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            response.getWriter().println("Erreur lors de la sauvegarde du fichier : " + ex.getMessage());
            return;
        }

        String nomGroupe = request.getParameter("nomGroupe");
        System.out.println("nomgroupe"+nomGroupe);
        String nomMatiere = request.getParameter("nomMatiere");
        int nbreEtudiant = Integer.parseInt(request.getParameter("nbreEtudiant"));

        String date = request.getParameter("date");
        int etat = (int) request.getSession().getAttribute("id");
        LocalDate localDate = LocalDate.parse(date);

// Convertir LocalDate en java.sql.Date
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
//        int etat = Integer.parseInt(request.getParameter("etat"));
        tn.iit.model.demande newDemande = new demande(nomGroupe, nomMatiere, nbreEtudiant, fileName,etat , sqlDate);
        try {
            demandeDao.insertDemande(newDemande);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.sendRedirect("demandes/list2");
        ;
    }
}
