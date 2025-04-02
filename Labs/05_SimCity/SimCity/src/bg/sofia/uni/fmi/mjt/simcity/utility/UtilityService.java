package bg.sofia.uni.fmi.mjt.simcity.utility;

import bg.sofia.uni.fmi.mjt.simcity.exception.UtilityTypeNotFoundException;
import bg.sofia.uni.fmi.mjt.simcity.exception.messages.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.simcity.property.billable.Billable;

import java.util.HashMap;
import java.util.Map;

public class UtilityService implements UtilityServiceAPI {
    private Map<UtilityType, Double> taxRates;

    public UtilityService(Map<UtilityType, Double> taxRates) {
        setTaxRates(taxRates);
    }

    private void setTaxRates(Map<UtilityType, Double> taxRates) {
        this.taxRates = taxRates;
    }

    @Override
    public <T extends Billable> double getUtilityCosts(UtilityType utilityType, T billable) {
        validateUtility(utilityType);
        validateBillable(billable);

        double consumption = switch (utilityType) {
            case WATER -> billable.getWaterConsumption();
            case ELECTRICITY -> billable.getElectricityConsumption();
            case NATURAL_GAS -> billable.getNaturalGasConsumption();
            default -> 1;
        };

        return taxRates.get(utilityType) * consumption;
    }

    @Override
    public <T extends Billable> double getTotalUtilityCosts(T billable) {
        validateBillable(billable);

        double result = 0.0;

        result += taxRates.get(UtilityType.WATER) * billable.getWaterConsumption();
        result += taxRates.get(UtilityType.ELECTRICITY) * billable.getElectricityConsumption();
        result += taxRates.get(UtilityType.NATURAL_GAS) * billable.getNaturalGasConsumption();

        return result;
    }

    @Override
    public <T extends Billable> Map<UtilityType, Double> computeCostsDifference(T firstBillable, T secondBillable) {
        validateBillable(firstBillable);
        validateBillable(secondBillable);

        double diffWater = getUtilityCosts(UtilityType.WATER, firstBillable)
                - getUtilityCosts(UtilityType.WATER, secondBillable);
        double diffElectricity = getUtilityCosts(UtilityType.ELECTRICITY, firstBillable)
                - getUtilityCosts(UtilityType.ELECTRICITY, secondBillable);
        double diffGas = getUtilityCosts(UtilityType.NATURAL_GAS, firstBillable)
                - getUtilityCosts(UtilityType.NATURAL_GAS, secondBillable);

        Map<UtilityType, Double> results = new HashMap<>();

        results.put(UtilityType.WATER, Math.abs(diffWater));
        results.put(UtilityType.ELECTRICITY, Math.abs(diffElectricity));
        results.put(UtilityType.NATURAL_GAS, Math.abs(diffGas));

        return Map.copyOf(results);
    }

    private void validateUtility(UtilityType type) {
        if (type == null) {
            throw new IllegalArgumentException(ExceptionMessages.UTILITY_TYPE_NULL);
        }

        if (!taxRates.containsKey(type)) {
            throw new UtilityTypeNotFoundException(ExceptionMessages.UTILITY_SERVICE_DOES_NOT_CONTAINS + type);
        }
    }

    private <T extends Billable> void validateBillable(T billable) {
        if (billable == null) {
            throw new IllegalArgumentException(ExceptionMessages.BUILDABLE_NULL);
        }
    }
}
