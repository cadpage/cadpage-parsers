package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Lincoln County, NC
 */
public class NCLincolnCountyParser extends DispatchOSSIParser {

  private static Pattern CODE_PTN = Pattern.compile("\\b\\d{1,2}-[A-Z]-\\d{1,2}\\b *");
  private static Pattern CODE_PTN2 = Pattern.compile("\\b(\\d{1,2}[A-Z]\\d{1,2}-) *");
  private static Pattern SQBRACE_DASH_PTN = Pattern.compile("\\] *-");
  private static Pattern DASH_PTN = Pattern.compile("(?<! )-(?! )");

  public NCLincolnCountyParser() {
    super("LINCOLN COUNTY", "NC",
           "ID: ( CANCEL ADDR! INFO/D+ " +
               "| FYI? SRC? ID? CODE? CALL CALL2/D? COUNTY? ( PHONE NAME | SECURITY | ) PARTADDR? ADDR! EMPTY+? " +
                   "( SELECT/2 INFO/D+? X X+ " +
                   "|  ( X X? | PLACE APT X+? | PLACE X X? | PLACE X/Z X| ) INFO/D+ " +
                   ") " +
               ")");
  }

  @Override
  public String getFilter() {
    return "CAD@lincolne911.org,CAD@lincolnsheriff.org,cad@do-not-reply-lincolne911.org,93001,777";
  }

  private static final Pattern DATE_TIME_MARK_PTN = Pattern.compile(" *\\[\\d\\d/\\d\\d/\\d\\d \\d\\d?:\\d\\d:\\d\\d: [A-Z]+\\] *");
  private static final Pattern ALT_VARIANT_PTN = Pattern.compile(".*[a-z\\[\\]].*-(.*)");

  @Override
  public boolean parseMsg(String body, Data data) {

    int pt = body.indexOf("\nDISCLAIMER:");
    if (pt >= 0) body = body.substring(0,pt).trim();

    // Remove dashes in street names
    body = body.replace("HOOVER-ELMORE", "HOOVER ELMORE");
    body = body.replace("7-ELEVEN", "7 ELEVEN");
    body = body.replace("RENT-A-CAR", "RENT A CAR");

    // Alternate format moves the cross streets to the end
    Matcher match = ALT_VARIANT_PTN.matcher(body);
    setSelectValue(match.matches() && isValidCrossStreet(match.group(1).trim()) ? "2" : "1");

    // The OSSI parser either expects a leading ID field, or does not expect one.  It can't handle our case
    // where it sometimes is there and sometimes isn't.  We fix that by adding a dummy ID if there isn't one.
    if (body.startsWith("CAD:")) body = "0:" + body;

    // For medical call, the code is duplicated and the one in the call
    // description has dashes, which we are going to use a field separators
    // Easy solution is to just get rid of it.
    body = CODE_PTN.matcher(body).replaceFirst("");

    // Sometimes the code isn't duplicated, but is followed by a dash blank for
    // which we must eliminate the blank.
    body = CODE_PTN2.matcher(body).replaceFirst("$1");

    // More fixes
    body = body.replace("FYI: -", "FYI:-").replace("Update: -", "Update:-");
    body = SQBRACE_DASH_PTN.matcher(body).replaceAll("]");

    // Change dashes to regular semicolon field separators
    // Make that dashes with non-blanks on both sides to try to miss regular
    // dashes inside data fields :(
    body = DASH_PTN.matcher(body).replaceAll(";");
    while (body.endsWith("-")) body = body.substring(0,body.length()-1).trim();

    // And convert comment marks to line breaks
    body = DATE_TIME_MARK_PTN.matcher(body).replaceAll("\n");

    if (! super.parseMsg(body, data)) return false;
    if (data.strCallId.equals("0")) data.strCallId = "";
    return true;
  }

  @Override
  public boolean isValidCrossStreet(String field) {
    return field.endsWith(" RAMP") || super.isValidCrossStreet(field);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z][A-Z0-9]{2,4}", true);
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("CALL2"))  return new CallField("MAT", true);
    if (name.equals("COUNTY")) return new CityField(".* CO", true);
    if (name.equals("PHONE")) return new PhoneField("\\d{7,}", true);
    if (name.equals("SECURITY")) return new NameField(".*SECURITY.*", true);
    if (name.equals("PARTADDR")) return new MyPartAddressField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceAddress();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyIdField extends IdField {
    public MyIdField() {
      setPattern(Pattern.compile("\\d{9,}|"));
    }
  }

  private class MyCodeField extends CodeField {
    public MyCodeField() {
      setPattern(Pattern.compile("\\d{2,3}[A-Z]\\d{2}[A-Za-z]?"));
    }
  }

  private class MyPartAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!NUMERIC.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      data.strAddress = field;
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, "-", field);
      data.strAddress = "";
      super.parse(field, data);
    }
  }


  private class MyPlaceAddress extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() > 40) {
        data.strSupp = append(data.strSupp, " / ", field);
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE INFO";
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("\\d{1,4}|[A-J]");
  private static final Pattern PLACE_PTN = Pattern.compile("\\D.*");
  private class MyAptField extends AptField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      // Not valid unless previous place field is non-empty and starts with non-digit
      if (!PLACE_PTN.matcher(getRelativeField(-1)).matches()) return false;

      // And has to  match expected pattern
      if (!APT_PTN.matcher(field).matches()) return false;

      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.endsWith(" RAMP")) {
        super.parse(field, data);
        return true;
      } else {
        return super.checkParse(field, data);
      }
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +-- +");
  private class MyInfoField extends InfoField {
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      super.parse(field, data);
    }
  }
}
