package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

/**
 * Fresno County, CA
 */
public class CAFresnoCountyParser extends FieldProgramParser {

  public CAFresnoCountyParser() {
    super("FRESNO COUNTY", "CA",
          "( Unit:UNIT! Pri:PRI! Loc:ADDR! MapPage:MAP Apt:APT! City:CITY? Nature:CALL% Zone:MAP% EMS#:ID% XStreet:X% " +
          "| CALL! For:UNIT! ( Zone:MAP_ADDR! | Dist:MAP Address:ADDR ) Apt:APT! Between:X! Location_Name:PLACE! )");
  }

  @Override
  public String getFilter() {
    return "VCMail@co.fresno.ca.us,VCMail@fresnocountyca.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }


  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public int splitBreakLength() { return 255; }
      @Override public int splitBreakPad() { return 1; }
    };
  }

  private static final Pattern ADDR_CHG_PTN = Pattern.compile("Unit:(\\S+) *AddressChanged From:.*?,to: (.*)");
  private static final Pattern PRI_CHG_PTN = Pattern.compile("Unit:(\\S+) +The Priority of the call at (.*?) has been changed to P(\\d) .*");
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(?:Unit:?(.+?)[, ]+)?(?:EMS|Incident |Run)#:(\\d*)[ ,]+(.*)");
  private static final Pattern RUN_REPORT_BRK = Pattern.compile(" +,");
  private static final Pattern CANCEL_PTN = Pattern.compile("Unit:(.+?)[ ]+EMS:(\\S*) *(Incident Cancelled:.*?) +(CR:.*)");
  private static final Pattern CANCEL_BRK_PTN = Pattern.compile("(?<=\\d\\d:\\d\\d:\\d\\d)(?=[A-Z])");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("VisiCad Email")) return false;

    if (body.startsWith("Unit") || body.startsWith("EMS#:")) {

      Matcher match = ADDR_CHG_PTN.matcher(body);
      if (match.matches()) {
        setFieldList("UNIT CALL ADDR APT X MAP");
        data.strUnit = match.group(1);
        data.strCall = "Address change";
        FParser fp = new FParser(match.group(2));
        parseAddress(fp.get(30), data);
        data.strCross = fp.get(60);
        data.strMap = fp.get();
        return true;
      }

      else if ((match = PRI_CHG_PTN.matcher(body)).matches()) {
        setFieldList("UNIT CALL ADDR APT PRI");
        data.strUnit =  match.group(1);
        data.strCall = "Priority change";
        parseAddress(match.group(2).trim(), data);
        data.strPriority = match.group(3);
        return true;
      }

      else if ((match = RUN_REPORT_PTN.matcher(body)).matches()) {
        setFieldList("UNIT ID INFO");
        data.msgType = MsgType.RUN_REPORT;
        data.strUnit = getOptGroup(match.group(1));
        data.strCallId = match.group(2);
        data.strSupp = RUN_REPORT_BRK.matcher(match.group(3)).replaceAll("\n");
        return true;
      }

      else if ((match = CANCEL_PTN.matcher(body)).matches()) {
        setFieldList("UNIT ID CALL INFO");
        data.msgType = MsgType.RUN_REPORT;
        data.strUnit = match.group(1);
        data.strCallId = match.group(2);
        data.strCall = match.group(3).trim();
        data.strSupp = CANCEL_BRK_PTN.matcher(match.group(4).replace(" ", "")).replaceAll("\n");
        return true;
      }

      if (parseFields(body.split(","), 6, data)) return true;
    }

    else {
      body = body.replace("Apt:", " Apt:").replace("Location Name:", " Location Name:");
      if (super.parseMsg(body, data)) return true;
    }

    setFieldList("INFO");
    data.parseGeneralAlert(this, body);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("MAP_ADDR")) return new MyMapAddressField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("MAP")) return new MyMapField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "*");
      super.parse(field, data);
    }
  }

  private static final Pattern ADDR_MAP_PTN = Pattern.compile("(.*?) {3,}(.*)");
  private class MyMapAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (!match.find()) abort();
      data.strMap = getOptGroup(match.group(1));
      super.parse(match.group(2), data);
    }

    @Override
    public String getFieldNames() {
      return "MAP " + super.getFieldNames();
    }
  }

  private static final Pattern APT_PREFIX_PTN = Pattern.compile("^(?:#|APT|ROOM|RM|STE) *", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PREFIX_PTN.matcher(field);
      if (match.find()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, "-", field);
    }
  }
}
