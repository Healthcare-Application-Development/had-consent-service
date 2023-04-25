package com.example.hadconsentservice.service;
import com.example.hadconsentservice.bean.ConsentArtifact;
import com.example.hadconsentservice.bean.ConsentItem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hadconsentservice.repository.ConsentArtifactRepository;
import com.example.hadconsentservice.repository.ConsentItemRepository;
@Service
public class ConsentService {

    private final ConsentArtifactRepository consentArtifactRepository;
    private final ConsentItemRepository consentItemRepository;
    @Autowired
    ConsentArtifactService consentArtifactService;
    public ConsentService(ConsentArtifactRepository consentArtifactRepository,
                          ConsentItemRepository consentItemRepository) {
        this.consentArtifactRepository = consentArtifactRepository;
        this.consentItemRepository = consentItemRepository;
    }

    public boolean saveConsentArtifactWithItems(ConsentArtifact consentArtifact) {
        try {
            consentArtifactRepository.save(consentArtifact);
            for (ConsentItem consentItem : consentArtifact.getConsentItems()) {
                consentItem.setConsentArtifact(consentArtifact);
                consentItemRepository.save(consentItem);
            }
            return true;
        } catch (Exception e) {
            // handle exception
            System.out.println(e.getMessage());
            return false;
        }
    }


    public List<ConsentArtifact> revokeConsentArtifactItem(Integer Id) {
        ConsentItem consentitem = consentItemRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("ConsentArtifact not found with artifactId: " + Id));
        consentitem.setRevoked(true);
        consentItemRepository.save(consentitem);
        ConsentArtifact artifact = consentitem.getConsentArtifact();
        List<ConsentItem> consentItems = artifact.getConsentItems();
        boolean revoked = true;
        for (ConsentItem cItem: consentItems) {
            if (cItem.getConsentAcknowledged() && cItem.getApproved() && !cItem.isRevoked()) {
                revoked = false;
                break;
            } 
        }
        artifact.setRevoked(revoked);

        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();


            FileWriter myWriter = new FileWriter("logs/revoked_consents.txt", true);
            BufferedWriter br = new BufferedWriter(myWriter);
            br.write("\n"+dtf.format(now).toString() + ";" +artifact.getArtifactId().toString()+";"+artifact.getPatientID()+";"+artifact.getDoctorID()+";"+artifact.getRevoked()+"\n");
            br.close();
            myWriter.close();

        } catch (Exception e){
            System.out.println("Logging Failed");
            System.out.println(e.getMessage());
        }


        consentArtifactRepository.save(artifact);

        return consentArtifactService.findAllByPatientID(consentitem.getPatientID());
    }



}
