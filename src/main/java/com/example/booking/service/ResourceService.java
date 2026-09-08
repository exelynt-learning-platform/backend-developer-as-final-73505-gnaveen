package com.example.booking.service;

import com.example.booking.dto.ResourceRequest;
import com.example.booking.dto.ResourceResponse;
import com.example.booking.entity.Resource;
import com.example.booking.exception.ResourceNotFoundException;
import com.example.booking.repository.ResourceRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<ResourceResponse> getAllResources() {

        return resourceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ResourceResponse getResourceById(Long id) {

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resource not found with id: " + id
                        )
                );

        return mapToResponse(resource);
    }

    public ResourceResponse createResource(ResourceRequest request) {

        Resource resource = new Resource();

        resource.setName(request.getName());
        resource.setType(request.getType());
        resource.setAvailable(request.getAvailable());

        Resource savedResource = resourceRepository.save(resource);

        return mapToResponse(savedResource);
    }

    public ResourceResponse updateResource(Long id, ResourceRequest request) {

        Resource existingResource = resourceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resource not found with id: " + id
                        )
                );

        existingResource.setName(request.getName());
        existingResource.setType(request.getType());
        existingResource.setAvailable(request.getAvailable());

        Resource updatedResource = resourceRepository.save(existingResource);

        return mapToResponse(updatedResource);
    }

    public void deleteResource(Long id) {

        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Resource not found with id: " + id
            );
        }

        resourceRepository.deleteById(id);
    }

    private ResourceResponse mapToResponse(Resource resource) {

        return new ResourceResponse(
                resource.getId(),
                resource.getName(),
                resource.getType(),
                resource.isAvailable()
        );
    }
}