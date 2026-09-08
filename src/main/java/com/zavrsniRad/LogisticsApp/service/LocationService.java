package com.zavrsniRad.LogisticsApp.service;

import com.zavrsniRad.LogisticsApp.domain.dto.location.CreateUpdateLocation;
import com.zavrsniRad.LogisticsApp.domain.dto.location.PatchLocation;
import com.zavrsniRad.LogisticsApp.domain.dto.shipment.LocationResponse;
import com.zavrsniRad.LogisticsApp.domain.models.shipment.Location;
import com.zavrsniRad.LogisticsApp.exceptions.ResourceNotFoundException;
import com.zavrsniRad.LogisticsApp.mappers.LocationMapper;
import com.zavrsniRad.LogisticsApp.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public Page<LocationResponse> getAllLocations(Pageable pageable){

        return locationRepository.findAll(pageable).map(locationMapper::toResponse);

    }

    public LocationResponse getLocation(UUID id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location with the provided ID: " + id + " doesn't exist."));

        return locationMapper.toResponse(location);

    }

    @Transactional
    public LocationResponse createLocation(CreateUpdateLocation createUpdateLocation) {

        Location newLocation = Location
                .builder()
                .name(createUpdateLocation.getName())
                .address(createUpdateLocation.getAddress())
                .city(createUpdateLocation.getCity())
                .country(createUpdateLocation.getCountry())
                .latitude(createUpdateLocation.getLatitude())
                .longitude(createUpdateLocation.getLongitude())
                .build();

        Location saved = locationRepository.save(newLocation);

        return locationMapper.toResponse(saved);
    }

    @Transactional
    public LocationResponse updateLocation(UUID id, CreateUpdateLocation updateLocation) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location with the provided ID: " + id + " doesn't exist."));

        location.setName(updateLocation.getName());
        location.setAddress(updateLocation.getAddress());
        location.setCity(updateLocation.getCity());
        location.setCountry(updateLocation.getCountry());
        location.setLatitude(updateLocation.getLatitude());
        location.setLongitude(updateLocation.getLongitude());

        Location savedLocation = locationRepository.save(location);

        return locationMapper.toResponse(savedLocation);

    }

    @Transactional
    public LocationResponse patchLocation(UUID id, PatchLocation updateLocation) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location with the provided ID: " + id + " doesn't exist."));

        if (updateLocation.getName() != null){
            location.setName(updateLocation.getName());
        }

        if (updateLocation.getAddress() != null){
            location.setAddress(updateLocation.getAddress());
        }

        if (updateLocation.getCity() != null){
            location.setCity(updateLocation.getCity());
        }

        if (updateLocation.getCountry() != null){
            location.setCountry(updateLocation.getCountry());
        }

        if (updateLocation.getLatitude() != null){
            location.setLatitude(updateLocation.getLatitude());
        }

        if (updateLocation.getLongitude() != null){
            location.setLongitude(updateLocation.getLongitude());
        }

        Location savedLocation = locationRepository.save(location);

        return locationMapper.toResponse(savedLocation);

    }

    @Transactional
    public void deleteLocation(UUID id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location with the provided ID: " + id + " doesn't exist."));

        locationRepository.delete(location);
    }
}
