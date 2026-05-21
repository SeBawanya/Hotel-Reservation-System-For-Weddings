package com.example.hotelreservationsysyemforweddings.service;

import com.example.hotelreservationsysyemforweddings.model.Ballroom;
import com.example.hotelreservationsysyemforweddings.repository.BallroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BallroomService {

    @Autowired
    private BallroomRepository ballroomRepository;

    public List<Ballroom> getAllBallrooms() {
        return ballroomRepository.findAll();
    }

    public Ballroom createBallroom(Ballroom ballroom) {
        return ballroomRepository.save(ballroom);
    }

    public Optional<Ballroom> updateBallroom(Long id, Ballroom ballroomDetails) {
        return ballroomRepository.findById(id).map(existingBallroom -> {
            existingBallroom.setName(ballroomDetails.getName());
            return ballroomRepository.save(existingBallroom);
        });
    }

    public void deleteBallroom(Long id) {
        ballroomRepository.deleteById(id);
    }
}
