package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchDAPROParser;

/**
 * Appomattox County, VA
 */
public class VAAppomattoxCountyParser extends DispatchDAPROParser {
  
  public VAAppomattoxCountyParser() {
    super(CITY_CODE_TABLE, "APPOMATTOX COUNTY", "VA");
    setupCallList(CALL_SET);
  }
  
  @Override
  public String getFilter() {
    return "MAILBOX@appomattoxcountyva.gov";
  }
  
  
  private static final CodeSet CALL_SET = new CodeSet(
    "911 ABANDONED CALL",
    "911 CALL RECEIVED",
    "911 HANG UP",
    "ABDOMINAL DISTRESS",
    "ACCIDENT",
    "ALARM (FIRE)",
    "ALARM (MEDICAL)",
    "ALLERGIC REACTION",
    "BREATHING DIFFICULTY",
    "BRUSH FIRE",
    "CARDIAC (WITH CARDIAC HISTORY)",
    "CHEST PAIN (NO CARDIAC HISTORY",
    "DIABETIC ILLNESS/INSULIN REACT",
    "DISORIENTED",
    "DOMESTIC TROUBLE",
    "FALL/FRACTURE",
    "FEVER",
    "HEADACHE",
    "HYPERTENSION HIGH BLOOD PRESSU",
    "LIFTING ASSISTANCE",
    "MENTAL SUBJECT",
    "MISCELLANEOUS",
    "MOTOR VEHICLE ACCIDENT",
    "NAUSEA/VOMITING",
    "OB/GYN (PREGNANCY/MISCARRIAGE)",
    "OTHER (DESCRIBE IN REMARKS)",
    "PAIN",
    "SEIZURE/CONVULSIONS",
    "SICK (UNKNOWN MEDICAL)",
    "STROKE",
    "UNRESPONSIVE"
  );
  
  private static final Properties CITY_CODE_TABLE =
    buildCodeTable(new String[]{
        "APP", "APPOMATTOX",
        "GLA", "GLADSTONE",
        "SPO", "SPOUT SPRING",
        "PAM", "PAMPLIN CITY"
    });
}