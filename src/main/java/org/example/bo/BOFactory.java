package org.example.bo;

import org.example.bo.custom.impl.*;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {

    }
    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {
        STUDENT, PROGRAMS,USER,PAYMENT,ENROLLMENT
    }

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case STUDENT:
                return new StudentBoImpl();
            case PROGRAMS:
                return new ProgramBoImpl();
                case USER:
                    return new UserBoImpl();
                    case PAYMENT:
                        return new PaymentBoImpl();
                        case ENROLLMENT:
                            return new EnrollmentBoImpl();
            default:
                return null;

        }
    }
}
