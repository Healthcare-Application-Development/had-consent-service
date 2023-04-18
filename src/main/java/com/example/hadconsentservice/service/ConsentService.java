package com.example.hadconsentservice.service;
import com.example.hadconsentservice.bean.ConsentArtifact;
import com.example.hadconsentservice.bean.ConsentItem;
import org.springframework.stereotype.Service;
import com.example.hadconsentservice.repository.ConsentArtifactRepository;
import com.example.hadconsentservice.repository.ConsentItemRepository;
@Service
public class ConsentService {

    private final ConsentArtifactRepository consentArtifactRepository;
    private final ConsentItemRepository consentItemRepository;

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


    public ConsentItem revokeConsentArtifactitem(Integer Id) {
        ConsentItem consentitem = consentItemRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("ConsentArtifact not found with artifactId: " + Id));

        consentitem.setRevoked(true);
        consentItemRepository.save(consentitem);

        return consentitem;
    }



}
