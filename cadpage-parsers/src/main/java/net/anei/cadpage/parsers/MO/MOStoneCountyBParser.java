package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.dispatch.DispatchA48Parser;

/**
 * Stone County, MO (B)
 */
public class MOStoneCountyBParser extends DispatchA48Parser {
  
  public MOStoneCountyBParser() {
    super(CITY_LIST, "STONE COUNTY", "MO", FieldType.X, A48_ONE_WORD_CODE);
  }
  
  @Override
  public String getFilter() {
    return "777,CAD@sces911.org";
  }

  // Call codes usually contain no blanks.  But very rarely one comes through
  // with the dashes replaced with blanks, and we need to fix that
  @Override
  public String fixCallAddress(String addr) {
    String code = CALL_LIST.getCode(addr, true);
    if (code != null) {
      addr = code.replace(' ', '-') + addr.substring(code.length());
    }
    return addr;
  }
  
  private static final CodeSet CALL_LIST = new  CodeSet(
      "Allergic 1",
      "Breathing",
      "Chest Pain",
      "Chest Pain 2",
      "Diabetic 2",
      "Fainting 1",
      "Fainting 2",
      "Fall 1",
      "F Structure Contained",
      "F Structure Small/Non Dwelling",
      "Headache 1",
      "Heart 1",
      "Hemorrhage 1",
      "MutualAid",
      "MVC",
      "Seizure 1",
      "Sick 1",
      "Sick 2",
      "Stroke",
      "Suicide Attempt",
      "Suicide Attempt W",
      "Traumatic 1",
      "Unconscious",
      "Well Being"
  );

  private static final String[] CITY_LIST = new String[]{
    "BILLINGS",
    "BLUE EYE",
    "BRANSON",
    "BRANSON WEST",
    "CAPE FAIR",
    "CARR LANE",
    "CONEY ISLAND",
    "CRANE",
    "CROSSROADS",
    "ELSEY",
    "GALENA",
    "HURLEY",
    "INDIAN POINT",
    "KIMBERLING CITY",
    "LAMPE",
    "MCCORD BEND",
    "PONCE DE LEON",
    "POSSUM TROT",
    "REEDS SPRING",
    "REEDS SPRING JUNCTION",
    "SHELL KNOB",
    "UNION CITY",
    "VIOLA"
  };
}
