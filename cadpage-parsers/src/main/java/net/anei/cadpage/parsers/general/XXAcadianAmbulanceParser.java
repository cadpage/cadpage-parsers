package net.anei.cadpage.parsers.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class XXAcadianAmbulanceParser extends FieldProgramParser {

  public XXAcadianAmbulanceParser(String defState) {
    super("", defState,
          "( SELECT/1 CALL! Loc:PLACE! Address:ADDR! Apt:APT! City:CITY! Parish:SKIP! Latitude:GPS1/d Longitude:GPS2/d" +
          "| Location:PLACE! Address:ADDR! Apt:APT! City:CITY! Changed_From:SKIP!" +
          "| CALL! Loc:PLACE! Add:ADDR! APT:APT? Cross_St:X! City:CITY! Cnty:CITY! Map_Pg:MAP Dest:INFO Pt's_Name:NAME )",
          FLDPROG_IGNORE_CASE);
  }

  @Override
  public String getAliasCode() {
    return "XXAcadianAmbulance";
  }

  @Override
  public String getFilter() {
    return "commcenter@acadian.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern MARKER = Pattern.compile("Resp(?:onse)?[#:]+(\\d+(?:-\\d{4})?|) +");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=Loc:|Add:|APT:|Cross St:|City:|Cnty:|Map Pg:|Dest:|Pt's Name:)");
  private static final Pattern RUN_REPORT_DELIM = Pattern.compile("(?<=\\d\\d:\\d\\d:\\d\\d)\\s*(?=[A-Z][A-Za-z]+:)");
  private static final Pattern DELIM2 = Pattern.compile("\\*(?=Loc:|Address:|Apt:|City:|Parish:)| +(?=Latitude:|Longitude:)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (match.find()) {
      setSelectValue("2");
      data.strCallId = match.group(1);
      body = body.substring(match.end());

      if (body.startsWith("Alerted:")) {
        setFieldList("INFO");
        data.msgType = MsgType.RUN_REPORT;
        data.strSupp = RUN_REPORT_DELIM.matcher(body).replaceAll("\n");
        return true;
      }

      if (body.startsWith("New Pri:")) {
        setFieldList("CALL INFO");
        data.strCall = "Priority Change";
        data.strSupp = MBLANK_PTN.matcher(body).replaceAll("\n");
        return true;
      }

      if (body.startsWith("Comment:")) {
        setFieldList("INFO");
        data.msgType = MsgType.GEN_ALERT;
        data.strSupp = body.substring(8).trim();
        return true;
      }

      if (body.startsWith("Changed To:Location:")) {
        body = body.substring(11);
        data.strCall = "Address Change";
        body = body.replace("Address:", " Address:").replace(" Apt.", " Apt:");
        return super.parseMsg(body,  data);
      }

      body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
      if (!super.parseMsg(body, data)) return false;

      // Fixe some state specific issues
      if (data.defState.equals("TX")) {

        // There is one particular long, oft abbreviated road in Harris County, TX
        // that always causes trouble
        if (data.strCity.toUpperCase().startsWith("HARRIS")) {
          int pt = data.strAddress.lastIndexOf(' ');
          if (pt >= 0) {
            pt = data.strAddress.lastIndexOf(' ', pt-1);
            if (pt >= 0) {
              String tag = data.strAddress.substring(pt+1).toUpperCase();
              if ("HUFFMAN CLEVELAND RD".startsWith(tag)) {
                data.strAddress = data.strAddress.substring(0,pt+1) + "Huffman-Cleveland Rd";
              }
            }
          }
        }
      }
      return true;
    }
    else if (body.contains("*Loc:")) {
      setSelectValue("1");
      return parseFields(DELIM2.split(body), data);
    }
    return false;
  }

  @Override
  public String getProgram() {
    return "ID CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = field.replaceAll("  +", " ");
      super.parse(field, data);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("UDC")) field = "San Antonio";
      if (data.strCity.endsWith(" County") && data.strCity.startsWith(field)) return;
      data.strCity = append(data.strCity, ", ", field);
    }
  }
}
