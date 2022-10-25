package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.HtmlDecoder;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchH03Parser extends FieldProgramParser {

  public DispatchH03Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchH03Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "SKIP+? DASHES ( INCIDENT_DETAILS%EMPTY! ( LOCATION:EMPTY! | LOCATION_of_Incident:EMPTY! ) ( Location:ADDR! | Loc:ADDR! ) Loc_Name:PLACE! Loc_Descr:INFO! " +
                            "City:CITY! Building:APT? Subdivision:APT? Floor:APT? Apt/Unit:APT! Zip_Code:ZIP? Cross_Strs:X? Area:MAP? Sector:MAP/D! Beat:MAP/D! Census_Tract:SKIP? RA:BOX? " +
                            "( Map_Book:MAP/C MAP/C+? DASHES! | DASHES? ) " +
                            "INCIDENT:EMPTY! Inc_#:ID2! Priority:PRI! Inc_Type:CODE! Descr:CALL! Mod_Circum:CALL/SDS! Created:TIMEDATE! Caller:NAME! Phone:PHONE! " +
                            "DASHES! ( SECONDARY_RESPONSE_LOCATION:EMPTY INFO/N+? DASHES! | ) UNITS_DISPATCHED:EMPTY! UNIT/S+? DASHES! PERSONNEL_DISPATCHED:EMPTY! SKIP! " +
                            "COMMENTS:EMPTY! INFO/N+? DASHES PREMIS_HAZARD:ALERT " +
                         "| INCIDENT:EMPTY! Inc_Type:CODE! Mod_Circum:CALL/SDS! Priority:PRI! Area:MAP! County:CITY! " +
                            "LOCATION:EMPTY! Loc_Name:PLACE! Loc_Descr:INFO! Location:ADDR! Municipality:CITY! Building:APT! Floor:APT! Apt/Unit:APT! Cross_Strs:X! " +
                            "PREMISE_HAZARD:EMPTY! INFO/N+ COMMENTS:EMPTY! INFO/N+ UNITS_DISPATCHED:EMPTY! UNIT/C+ Caller:NAME! Phone:PHONE! Created:TIMEDATE! Inc_#:ID " +
                         ")");
  }

  private HtmlDecoder decoder = new HtmlDecoder();

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!body.startsWith("<")) return super.parseHtmlMsg(subject, body, data);

    String[] flds = decoder.parseHtml(body);
    if (flds == null) return false;
    flds = condenseFields(flds);
    return parseFields(flds, data);
  }

  private String[] condenseFields(String[] flds) {
    List<String> fldList= new ArrayList<String>();
    boolean copy = false;
    String lastFld = null;
    for (String fld : flds) {
      if (fld.equals("UNITS DISPATCHED:")) copy = true;
      if (fld.startsWith("---")) {
        if (lastFld != null) {
          fldList.add(lastFld);
          lastFld = null;
        }
        fldList.add(fld);
      }
      else if (!copy && fld.endsWith(":")) {
        if (lastFld != null) fldList.add(lastFld);
        lastFld = fld;
      }
      else if (lastFld != null) {
        fldList.add(lastFld + fld);
        lastFld = null;
      } else {
        fldList.add(fld);
      }
    }
    if (lastFld != null) fldList.add(lastFld);
    return fldList.toArray(new String[fldList.size()]);
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("Dispatch Notification for Incident ([-A-Z0-9]{12,20})");
  private static final Pattern DELIM = Pattern.compile("\\s*\n\\s*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!parseFields(DELIM.split(body), data)) return false;
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) data.strCallId = match.group(1);
    return true;

  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ZIP")) return new BaseZipField();
    if (name.equals("TIMEDATE")) return new BaseTimeDateField();
    if (name.equals("DASHES")) return new SkipField("-{10,}");
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("MAP")) return new BaseMapField();
    if (name.equals("ID2")) return new BaseIdField();
    return super.getField(name);
  }

  private class BaseZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      super.parse(field, data);
    }
  }

  private static final Pattern TIME_DATE_PTN = Pattern.compile("(\\d\\d?:\\d\\d:\\d\\d [AP]M) (\\d\\d?/\\d\\d?/\\d{4})");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class BaseTimeDateField extends TimeDateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      setTime(TIME_FMT, match.group(1), data);
      data.strDate = match.group(2);
    }
  }

  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private class BaseMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(",")) return;
      super.parse(field, data);
    }
  }

  private class BaseIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCallId.length() > 0) return;
      data.strCallId = field;
    }
  }
}