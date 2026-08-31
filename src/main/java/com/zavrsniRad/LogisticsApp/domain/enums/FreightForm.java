package com.zavrsniRad.LogisticsApp.domain.enums;

import java.util.Set;

public enum FreightForm {
    GENERAL {
        @Override
        public Set<TrailerType> compatibleTrailerType() {
            return Set.of(TrailerType.CONTAINER, TrailerType.FLATBED, TrailerType.CURTAIN_SIDE);
        }
    },
    LIQUID {
        @Override
        public Set<TrailerType> compatibleTrailerType() {
            return Set.of(TrailerType.TANKER);
        }
    };


    public abstract Set<TrailerType> compatibleTrailerType();

    public boolean isCompatible(TrailerType trailerType) { return compatibleTrailerType().contains(trailerType);}
}
