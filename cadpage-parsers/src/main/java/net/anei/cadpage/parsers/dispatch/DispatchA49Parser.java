package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/*
Lafayette Parish, LA
*/

public class DispatchA49Parser extends FieldProgramParser {

  public DispatchA49Parser(String defCity, String defState) {
    super(defCity, defState, 
        "( CAD_Num:SKIP! Addr:ADDR! Times:EMPTY! INFO/R! INFO/N+ Rpt#:ID END " +
        "| ( Rpt#:ID! Addr:ADDR! Inc_Type:CODE! " +
          "| DATE_TIME_SRC! Addr:ADDR! Cross:X? Inc_Type:CODE? Juris:SKIP? Report_#:ID? ) REMARKS! EXTRA+ )");
  }
  
  private static final Pattern REMARKS_PTN = Pattern.compile("(\nRemarks)[: ]+");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    body = REMARKS_PTN.matcher(body).replaceFirst("$1:\n");
    return parseFields(body.split("\n+"), 5, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE_TIME_SRC")) return new MyDateTimeSourceField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("REMARKS")) return new SkipField("Remarks:", true);;
    if (name.equals("EXTRA")) return new MyExtraField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_SRC_PTN = Pattern.compile("Date:(\\d\\d/\\d\\d/\\d{4}) Time:(\\d\\d:\\d\\d)(?:EQPT:(\\S+)| Num:(\\S+)|)");
  private class MyDateTimeSourceField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_SRC_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strSource = getOptGroup(match.group(3));
      data.strCallId = getOptGroup(match.group(4));
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME SRC ID";
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "\".");
      super.parse(field, data);
    }
  }

  private static final Pattern EXTRA_ID_PTN  = Pattern.compile(">(?:RPT#|AC)< *([-\\d]+)");
  private static final Pattern EXTRA_CALL_PTN = Pattern.compile("F>>?IC< *(?:F\\.)? *(.*?)(?: \\d{6})?");
  private static final Pattern EXTRA_GPS_PTN = Pattern.compile("\\bLat=([-+]\\d+\\.\\d{4,}) Long=([-+]\\d+\\.\\d{4,})\\b");
  private static final Pattern EXTRA_TIME_OP_PTN = Pattern.compile("(.*) \\d{4},\\d{3}");
  private static final Pattern EXTRA_PREFIX_PTN = Pattern.compile("(?:[A-Z]>)?(?:>(?:AC|E9|IC)<)? *(?:[A-Z]\\.)? *(.*)");
  private static final Pattern EXTRA_PHONE_PTN1 = Pattern.compile("(?:\\d{3})?\\d{7}");
  private static final Pattern EXTRA_PHONE_PTN2 = Pattern.compile("(?:LOC = .*\\bP#|>P#< P-ANI:) *(\\(?\\d{3}\\)? \\d{3}-\\d{4})\\b.*", Pattern.CASE_INSENSITIVE);
  private static final Pattern EXTRA_LEAD_GT_PTN = Pattern.compile(">(?![A-Z]+<)");
  private class MyExtraField extends Field {
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = EXTRA_ID_PTN.matcher(field);
      if (match.matches()) {
        data.strCallId = match.group(1);
        return;
      }
      
      match = EXTRA_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCall = append(data.strCall, " / ", match.group(1));
        return;
      }
      
      match = EXTRA_GPS_PTN.matcher(field);
      if (match.find()) {
        setGPSLoc(match.group(1) + ',' + match.group(2), data);
        return;
      }
      
      match = EXTRA_TIME_OP_PTN.matcher(field);
      if (match.matches()) field = match.group(1).trim();
      
      match = EXTRA_PREFIX_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
     
      // Old data changes are not worth keeping
      if (field.startsWith(">UG<")) return;
      
      // Place name is worth extracting
      if (field.startsWith(">CP<")) {
        field = field.substring(4).trim();
        if (field.toUpperCase().startsWith("ALARM =")) {
          field = field.substring(7).trim();
          int pt = field.indexOf(',');
          if (pt >= 0) field = field.substring(pt+1).trim(); 
        }
        data.strPlace = append(data.strPlace, " - ", field);
        return;
      }
      
      if (EXTRA_PHONE_PTN1.matcher(field).matches()) {
        data.strPhone = field;
        return;
      }
      
      // Drop useless cell tower address
      if (field.startsWith("ADDR =")) return;
      
      // and coordinate comment
      if (field.startsWith("WPH2 COORDS.")) return;
      
      // Cell phone number
      match = EXTRA_PHONE_PTN2.matcher(field);
      if (match.matches()) {
        data.strPhone = match.group(1);
        return;
      }
      
      // Cell phone name
      if (field.startsWith("NAME =")) {
        data.strName = cleanWirelessCarrier(field.substring(6).trim());
        return;
      }
      
      match = EXTRA_LEAD_GT_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end()).trim();
      
      data.strSupp = append(data.strSupp, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "ID CALL PLACE NAME PHONE INFO GPS";
    }
  }
}
