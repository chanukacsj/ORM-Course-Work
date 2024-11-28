package org.example.dao;

import org.example.dao.custom.Impl.*;

public class DAOFactory {
    private static DAOFactory factory;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        return factory == null ? new DAOFactory() : factory;
    }

    public enum DAOTypes {
        STUDENT, PROGRAM, USER,PAYMENT,ENROLLMENT
    }

    public SuperDAO getDAO(DAOTypes types) {
        switch (types) {
            case STUDENT:
                return new StudentDAOImpl();
            case PROGRAM:
                return new ProgramDAOImpl();
            case USER:
                return new UserDAOImpl();
                case PAYMENT:
                    return new PaymentDAOImpl();
                    case ENROLLMENT:
                        return new EnrollmentDAOImpl();
        }
        return null;
    }

}
