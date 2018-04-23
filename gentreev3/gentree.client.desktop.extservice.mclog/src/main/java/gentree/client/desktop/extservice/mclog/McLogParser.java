package gentree.client.desktop.extservice.mclog;

import gentree.common.configuration.enums.Age;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vanilka on 22/04/2018.
 */
public class McLogParser {

    private static Map<String, Age> ageMap = generateMap();


    public static String takeNamSurnameFromChangeAgeLog(String log) {
        if (!log.contains(TemplatesAllowed.TEMPLATE_CHANGE_AGE)) return null;

        String target = StringUtils.substringBetween(log, "]", TemplatesAllowed.TEMPLATE_CHANGE_AGE).trim();


        System.out.println(target);

        return target;
    }


    public static String takeFirstSimNameFromMarriedLog(String log) {
        if (!log.contains(TemplatesAllowed.TEMPLATE_MARRIED)) return null;

        return StringUtils.substringBetween(log, TemplatesAllowed.TEMPLATE_MARRIED, " and ").trim();
    }

    public static String takeSecondSimNameFromMarriedLog(String log) {
        if (!log.contains(TemplatesAllowed.TEMPLATE_MARRIED)) return null;

        return StringUtils.substringBetween(log, "and", "and they now live").trim();
    }

    public static String takeHouseholdNameFromMarriedLog(String log) {
        if(!log.contains(TemplatesAllowed.TEMPLATE_MARRIED)) return null;
        return StringUtils.substringBetween(log, "and they now live in the", "household").trim();
    }

    public static Age getAgeFromString(String string) {
        return ageMap.getOrDefault(string, null);
    }

    private static Map<String, Age> generateMap() {
        Map<String, Age> target = new HashMap<>();
        target.put("Age.TEEN", Age.ADO);
        target.put("Age.YOUNGADULT", Age.YOUNG_ADULT);
        target.put("Age.ADULT", Age.ADULT);
        target.put("Age.CHILD", Age.CHILD);
        target.put("Age.TODDLER", Age.TOODLER);
        target.put("Age.SENIOR", Age.SENIOR);
        return target;
    }

}
