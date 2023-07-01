package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA38Parser;

public class MNBeckerCountyParser extends DispatchA38Parser {

  private static final Pattern CRN_PTN = Pattern.compile("\\bCR#(\\d+)", Pattern.CASE_INSENSITIVE);
  private static final Pattern HN_PTN = Pattern.compile("\\bH#(\\d+)", Pattern.CASE_INSENSITIVE);
  private static final Pattern SLASH_DIR_PTN = Pattern.compile(".([NSEW])(?:ORTH|OUTH|AST|EST)?B(?:OUND)?\\b");
  private static final Pattern DIR_BOUND_PTN = Pattern.compile("\\b([NSEW])(?:ORTH|OUTH|AST|EST)? BOUND\\b");
  private static final Pattern DIR_DOT_DIR_DOT_PTN = Pattern.compile("([NSEW])\\.?([EWB])\\.?");
  private static final Pattern CVO_PTN = Pattern.compile("\\b(?:CVO|CP)\\b");
  private static final Pattern DIR_OF_PTN = Pattern.compile("(?:[NSEW]|NORTH|SOUTH|EAST|WEST)\\.? OF\\b.*|AND .*");
  private static final Pattern APT_PTN = Pattern.compile("\\d+[A-Z]?|(?:APT|ROOM|RM|UNIT|LOT|SUITE) *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern PART_ADDR_PTN = Pattern.compile("\\d.*|HWY .*|US .*|MN .*|ST \\d.*|.*[/&].*");

  public MNBeckerCountyParser() {
    super("BECKER COUNTY", "MN");
    setupProtectedNames("TOWN AND COUNTRY");
    addExtendedDirections();
  }

  @Override
  public String getFilter() {
    return "Tac10@co.becker.mn.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace(" BALSAM AVE ST", " BALSAM AVE");
    if (!super.parseMsg(body, data)) return false;
    if (NUMERIC.matcher(data.strCity).find()) {
      data.strPlace = append(data.strPlace, ", ", data.strCity);
      data.strCity = "";
    } else if (data.strCity.equals("B") || data.strCity.equals("BAG")) data.strCity = "BAGLEY";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("ADDR", "ADDR PLACE X");
  }

  @Override
  protected void parseAddress(String field, Data data) {
    field = CRN_PTN.matcher(field).replaceAll("CR $1");
    field = HN_PTN.matcher(field).replaceAll("HWY $1");
    field = SLASH_DIR_PTN.matcher(field).replaceAll(" $1B");
    field = DIR_BOUND_PTN.matcher(field).replaceAll("$1B");
    field = DIR_DOT_DIR_DOT_PTN.matcher(field).replaceAll("$1$2");
    field = CVO_PTN.matcher(field).replaceAll("CO");
    field = field.replace("MOUNTAIN  ASH", "MOUNTAIN ASH");
    field = field.replace("LA BUDDE", "LABUDDE");
    field = field.replace(" HWT ", " HWY ");

    // They use double blank delimiters to add all kinds
    // of things to the address field.  We will see what we can
    // do to set things aright.
    int pt = field.indexOf("  ");
    if (pt < 0) {
      parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_NO_IMPLIED_APT, field, data);
      String place = getLeft();
      if (place.startsWith("/") || place.startsWith("&")) {
        data.strAddress = append(data.strAddress, " & ", place.substring(1).trim());
      } else if (DIR_OF_PTN.matcher(place).matches()) {
        data.strAddress = append(data.strAddress, " ", place.replace(".", ""));
      } else {
        data.strPlace = place;
      }
      return;
    }

    String part1 = field.substring(0,pt);
    String part2 = field.substring(pt+2).trim();
    Matcher match = APT_PTN.matcher(part2);
    if (match.matches()) {
      String apt = match.group(1);
      if (apt == null) apt = part2;
      super.parseAddress(part1, data);
      data.strApt = apt;
      return;
    }

    match = APT_PTN.matcher(part1);
    if (match.matches()) {
      String apt = match.group(1);
      if (apt != null) {
        super.parseAddress(part2, data);
        data.strApt = apt;
        return;
      }
    }

    if (part2.startsWith("BETWEEN ")) {
      data.strCross = part2.substring(8).trim();
      super.parseAddress(part1, data);
      return;
    }

    if (!part2.startsWith("BY ") && isValidAddress(part2)) {
      if (PART_ADDR_PTN.matcher(part1).matches()) {
        parseAddress(part1 + ' ' + part2, data);
      } else {
        data.strPlace = part1;
        super.parseAddress(part2, data);
      }
      return;
    }

    if (checkAddress(part2) > checkAddress(part1)) {
      super.parseAddress(part2, data);
      data.strPlace = part1;
    } else {
      data.strAddress = part1;
      data.strPlace = part2;
    }
  }
}
