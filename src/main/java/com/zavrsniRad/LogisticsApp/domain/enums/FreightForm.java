package com.zavrsniRad.LogisticsApp.domain.enums;

import java.util.Set;

public enum FreightForm {
    GENERAL {
        @Override
        public Set<TrailerType> compatibleTrailerType() {
            return Set.of(TrailerType.CONTAINER, TrailerType.FLATBED, TrailerType.CURTAIN_SIDE);
        }

        @Override
        public Set<Capability> requiredCapabilities() {
            return Set.of();
        }
    },
    LIQUID {
        @Override
        public Set<TrailerType> compatibleTrailerType() {
            return Set.of(TrailerType.TANKER);
        }

        @Override
        public Set<Capability> requiredCapabilities() {
            return Set.of(Capability.LIQUID_REQ);
        }
    };


    public abstract Set<TrailerType> compatibleTrailerType();
    public abstract Set<Capability> requiredCapabilities();

    public boolean isCompatible(TrailerType trailerType) { return compatibleTrailerType().contains(trailerType);}
}
