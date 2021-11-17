package by.k19.model;


public enum UserType {
    ADMIN("Администратор"),
    DIRECTOR("Директор"),
    MANAGER("Менеджер торгового зала");

    private final String name;

    UserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
