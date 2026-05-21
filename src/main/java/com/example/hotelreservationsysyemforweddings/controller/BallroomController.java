package com.example.hotelreservationsysyemforweddings.controller;

import com.example.hotelreservationsysyemforweddings.model.Ballroom;
import com.example.hotelreservationsysyemforweddings.service.BallroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ballrooms")
public class BallroomController {

    @Autowired
    private BallroomService ballroomService;

    @GetMapping
    public List<Ballroom> getAllBallrooms() {
        return ballroomService.getAllBallrooms();
    }

    @PostMapping
    public Ballroom createBallroom(@RequestBody Ballroom ballroom) {
        return ballroomService.createBallroom(ballroom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ballroom> updateBallroom(@PathVariable Long id, @RequestBody Ballroom ballroomDetails) {
        return ballroomService.updateBallroom(id, ballroomDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBallroom(@PathVariable Long id) {
        ballroomService.deleteBallroom(id);
        return ResponseEntity.ok().build();
    }
}
