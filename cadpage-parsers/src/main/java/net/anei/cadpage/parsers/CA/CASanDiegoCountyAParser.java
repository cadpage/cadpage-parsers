package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * San Diego County, CA
 */
public class CASanDiegoCountyAParser extends FieldProgramParser {

  public CASanDiegoCountyAParser() {
    super("SAN DIEGO COUNTY", "CA",
          "( ALRM:PRI! MSTR_INC#:ID! TYP:CALL! ADDR:ADDR! LOC:PLACE! APT-SP:APT! TB:MAP% MAP:MAP/D% XST:X% TG:CH% TIMEDSP:TIME% UNITS:UNIT% INFO/N+ RPNAME:NAME% TEXT:INFO/N+ " +
          "| NAT:CALL! ( LOC:ADDR! APT:APT! CH:CH! UNIT:UNIT! " +
                      "| ADR:ADDR! ADDR2 APT:APT ( TAC:CH! | CHNL:CH ) UNITS:UNIT MAP:MAP% XST:X% ( X/Z SRC ID | SRC ID ) " +
                      ") END " +
          "| NOTIFICATION:CALL! ALARM:PRI! LOC:PL_ADDR! ON:CH! AT:DATETIME! " +
          "| INC#:ID! ADR:ADDR! ADDR2+ APT:APT! PLACE! TAC:CH! LOC:PLACE! " +
          "| ALM:PRI? MSTR_INC#:ID? TYP:CALL! ADR:ADDR! LOC:PLACE! XST:X? SP:APT% TB:MAP% MAP:MAP% TG:CH% TIMEDSP:TIME? UNITS:UNIT INFO+ LOC_CMNTS:INFO+ )");
  }

  @Override
  public String getFilter() {
    return "cad@jpapage.net,cad@sdrecc.org,CADPaging@heartlandfire.net,2083399220,8333280613";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("INFO:(.*)\\\\(DISP:.*)");
  private static final Pattern DELIM = Pattern.compile(" *\\\\+ *");
  private static final Pattern TRAIL_ID_PTN = Pattern.compile("(.*?) +(\\d{4}-\\d{6,8})");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.lastIndexOf("\nSTOP");
    if (pt < 0) pt = body.lastIndexOf("Text stop");
    if (pt >= 0) body = body.substring(0,pt).trim();

    do {
      if (subject.equals("CAD MESSAGE") || subject.equals("HCFA")) break;

      if (body.startsWith("/ CAD MESSAGE / ")) {
        body = body.substring(16).trim();
        break;
      }

      if (body.startsWith("HCFA PAGE: (HCFA) ")) {
        body = body.substring(18).trim();
        subject = "HCFA";
        break;
      }

      return false;
    } while (false);

    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ADDR APT INFO");
      data.msgType = MsgType.RUN_REPORT;
      parseAddress(match.group(1), data);
      data.strSupp = DELIM.matcher(match.group(2).trim()).replaceAll("\n");
      return true;
    }

    if (subject.equals("HCFA")) {
      if (body.contains("/ADR:")) {
        return parseFields(body.split("/"), data);
      } else {
        match = TRAIL_ID_PTN.matcher(body);
        if (match.matches()) {
          body = match.group(1);
          data.strCallId = match.group(2);
        }
        return parseMsg(body, data);
      }
    }
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " ID?";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PL_ADDR")) return new MyPlaceAddressField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{6,8}", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private class MyPlaceAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() > 20) {
        String place = field.substring(0,20).trim();
        field = field.substring(20).trim();
        if (!place.equals(field)) data.strPlace = place;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String save = data.strAddress;
      data.strAddress = "";
      super.parse(field, data);
      data.strAddress = append(save, " & ", data.strAddress);
    }
  }

  private static final Pattern APT_TITLE_PTN = Pattern.compile("^(?:APT|SUITE|SPT?|RM) *", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_TITLE_PTN.matcher(field);
      if (match.find()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }

  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, "-", field);
    }
  }

  private class MyChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ".");
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private static final Pattern EMPTY_PTN = Pattern.compile("[A-Z]+[:;]");
  private static final Pattern SKIP_PTN = Pattern.compile("(?:^| *,) *This incident \\d{4}.*$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (EMPTY_PTN.matcher(field).matches()) return;
      field = stripFieldStart(field, "TXT:[1]");
      field = SKIP_PTN.matcher(field).replaceAll("");
      super.parse(field, data);
    }
  }

}
