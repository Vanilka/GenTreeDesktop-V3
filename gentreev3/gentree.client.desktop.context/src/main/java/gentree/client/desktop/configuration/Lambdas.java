package gentree.client.desktop.configuration;

import gentree.client.desktop.domain.Member;
import gentree.client.desktop.domain.Relation;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vanilka on 17/04/2018.
 */
public class Lambdas {
    public static final Predicate<Relation> PREDICATE_SIM_LEFT_IS_NULL = r -> r.getLeft() == null;
    public static final Predicate<Relation> PREDICATE_SIM_RIGHT_IS_NULL = r -> r.getRight() == null;
    public static final Predicate<Relation> PREDICATE_REF_NUMBER_LARGER_THAN_0 = r -> r.getReferenceNumber() > 0;


    public static  Predicate<Relation> PREDICATE_VERIFY_SIM_LEFT_RIGHT_EQUAL(final Member rootSim) {
       return  (Relation relation) -> Objects.equals(relation.getLeft(), rootSim) || Objects.equals(relation.getRight(), rootSim);
    }

    public  static Predicate<Member> PREDICATE_COMPARE_MEMBER_ID_WITH_ID(final Long id) {
        return m -> m.getId() == id;
    }

    public static Predicate<Member>  PREDICATE_MEMBER_IS_NOT_EQUAL(final Member m) {
        return c -> !c.equals(m);
    }




}
