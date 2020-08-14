package net.anei.cadpage.parsers.MS;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;

public class MSHernandoParser extends DispatchB2Parser {

  private static final Pattern DIR_SLASH_OF_PTN = Pattern.compile("\\b([NSEW])/([BO])\\b");

  public MSHernandoParser() {
    super("E-911:", MSDeSotoCountyBParser.CITY_LIST, "HERNANDO", "MS");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "BLACK HAWK",
        "GRAYS CREEK",
        "GREEN DUCK",
        "GREEN MEADOW",
        "GREEN VILLAGE",
        "LITTLE DOG",
        "ROBERTSON GIN",
        "VALLEY GATE"
    );
  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN",
      "ACCIDENT UNKOWN INJURIES",
      "ACCIDENT W/INJURIES",
      "ALL OTHER FIRE",
      "BREATHING DIFFICULTIES",
      "CARBON MONOXIDE DETECTOR",
      "CHEST PAINS-MI",
      "COMPLAINT-RECKLESS DRIVING",
      "FALLS",
      "FIRE ALARM RESIDENCE",
      "HEADACHE",
      "SICK CALL PERSON",
      "UNCONSCIOUS/FAINTING",
      "WELFARE CONCERN"

  );

  @Override
  protected boolean parseAddrField(String field, Data data) {
    field = DIR_SLASH_OF_PTN.matcher(field).replaceAll("$1$2");
    return super.parseAddrField(field, data);
  }
}
