package net.anei.cadpage.parsers.NC;

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
           "ID: ( CANCEL ADDR! | FYI? SRC? ID? CODE? CALL ( PHONE NAME | ) PARTADDR? ADDR! ( X X? | PLACE X X? | ) ) INFO/D+");
  }

  @Override
  public String getFilter() {
    return "CAD@lincolne911.org,CAD@lincolnsheriff.org,cad@do-not-reply-lincolne911.org,93001,777";
  }

  private static final Pattern DATE_TIME_MARK_PTN = Pattern.compile(" *\\[\\d\\d/\\d\\d/\\d\\d \\d\\d?:\\d\\d:\\d\\d: [A-Z]+\\] *");

  @Override
  public boolean parseMsg(String body, Data data) {

    int pt = body.indexOf("\nDISCLAIMER:");
    if (pt >= 0) body = body.substring(0,pt).trim();

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
    return true;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z][A-Z0-9]{2,4}", true);
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("PHONE")) return new PhoneField("\\d{7,}", true);
    if (name.equals("PARTADDR")) return new MyPartAddressField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceAddress();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyIdField extends IdField {
    public MyIdField() {
      setPattern(Pattern.compile("\\d{9,}"));
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

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +-- +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      super.parse(field, data);
    }
  }
}
