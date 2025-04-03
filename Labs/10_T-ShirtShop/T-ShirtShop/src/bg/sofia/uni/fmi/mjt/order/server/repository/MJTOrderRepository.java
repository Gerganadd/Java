package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

public class MJTOrderRepository implements OrderRepository {
    private static final int INVALID_ID = -1;
    private int idCounter = 0;
    private Map<Integer, Order> successfulOrders;
    private List<Order> unsuccessfulOrders;

    public MJTOrderRepository() {
        successfulOrders = new LinkedHashMap<>();
        unsuccessfulOrders = new ArrayList<>();
    }

    @Override
    public Response request(String size, String color, String destination) {
        boolean isValidSize = validateSize(size);
        boolean isValidColor = validateColor(color);
        boolean isValidDestination = validateDestination(destination);

        if (isValidSize && isValidColor && isValidDestination) {
            idCounter++;

            TShirt tShirt = new TShirt(Size.valueOf(size), Color.valueOf(color));
            Order newOrder = new Order(idCounter, tShirt, Destination.valueOf(destination));
            successfulOrders.put(idCounter, newOrder);

            return Response.create(idCounter);
        } else {
            Size orderSize = isValidSize ? Size.valueOf(size) : Size.UNKNOWN;
            Color orderColor = isValidColor ? Color.valueOf(color) : Color.UNKNOWN;
            Destination orderDestination = isValidDestination ? Destination.valueOf(destination) : Destination.UNKNOWN;

            TShirt tShirt = new TShirt(orderSize, orderColor);
            Order newOrder = new Order(INVALID_ID, tShirt, orderDestination);
            unsuccessfulOrders.add(newOrder);

            return Response.decline(formatErrorMessage(size, color, destination));
        }
    }

    @Override
    public Response getOrderById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id can't be less or equal to 0");
        }

        if (successfulOrders.containsKey(id)) {
            return Response.ok(List.of(successfulOrders.get(id)));
        } else {
            return Response.notFound(id);
        }
    }

    @Override
    public Response getAllOrders() {
        List<Order> result = new ArrayList<>(successfulOrders.values());
        result.addAll(unsuccessfulOrders);

        return Response.ok(result);
    }

    @Override
    public Response getAllSuccessfulOrders() {
        return Response.ok(successfulOrders.values());
    }

    private String formatErrorMessage(String size, String color, String destination) {
        String sizeMessage = validateSize(size) ? null : "size";
        String colorMessage = validateColor(color) ? null : "color";
        String destinationMessage = validateDestination(destination) ? null : "destination";

        StringBuilder message = new StringBuilder("invalid: ");

        if (sizeMessage != null) {
            message.append(sizeMessage);
        }
        if (colorMessage != null) {
            if (sizeMessage != null) {
                message.append(',');
            }

            message.append(colorMessage);
        }
        if (destinationMessage != null) {
            if (sizeMessage != null || colorMessage != null) {
                message.append(',');
            }

            message.append(destinationMessage);
        }

        return message.toString();
    }

    private boolean validateSize(String size) {
        validateNotNullOrEmpty(size, "Size can't be null or empty");

        try {
            Size current = Size.valueOf(size);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    private boolean validateColor(String color) {
        validateNotNullOrEmpty(color, "Color can't be null or empty");

        try {
            Color current = Color.valueOf(color);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    private boolean validateDestination(String destination) {
        validateNotNullOrEmpty(destination, "Destination can't be null or empty");

        try {
            Destination current = Destination.valueOf(destination);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    private void validateNotNullOrEmpty(String text, String exceptionMessage) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
}
