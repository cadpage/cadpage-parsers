package net.anei.cadpage.parsers.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class XXAcadianAmbulanceParser extends FieldProgramParser {

  public XXAcadianAmbulanceParser(String defState) {
    super("", defState,
          "( SELECT/1 CALL! Loc:PLACE! Address:ADDR! Apt:APT! City:CITY! State:ST! Parish:SKIP! Latitude:GPS1/d Longitude:GPS2/d" +
          "| SELECT/CANCEL CALL ADDR! Loc:PLACE! Cancel_Reason:INFO! " +
          "| SELECT/ADDRCH Address:ADDR! Apt:APT! City:CITY! State:ST! Latitude:GPS1/d! Longitude:GPS2/d! " +
          "| Location:PLACE! Address:ADDR! ( Apt:APT! Bldg:APT/D? City:CITY! Changed_From:SKIP! " +
                                          "| Room:APT! City:CITY! Destination:LINFO! Address:LINFO! City:LINFO! " +
                                          ") " +
          "| CALL! Loc:PLACE! ( Add:ADDR! APT:APT? Bldg:APT/D? Cross_St:X! City:CITY! State:ST Cnty:CITY! Map_Pg:MAP Dest:DEST Pt's_Name:NAME Latitude:GPS1/d Longitude:GPS2/d" +
                             "| Address:ADDR! Apt:APT! Bldg:APT/D! Cross_St:X! City:CITY! State:ST! Parish/County:SKIP! Map_Pg:MAP! Notes:INFO! Dest:DEST! Latitude:GPS1/d! Longitude:GPS2/d " +
                             ") " +
          ")",
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

  private static final Pattern MARKER = Pattern.compile("Resp(?:onse)?[#:]+ *(\\d+(?:-\\d{4})?|) +");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=Loc:|Add:|Address:|APT:|Apt:|Bldg:|Cross St:|City:|State:|Cnty:|Parish/County:|Map Pg:|Notes:|Dest:|Pt's Name:|Latitude:|Longitude:)");
  private static final Pattern RUN_REPORT_DELIM = Pattern.compile("(?<=\\d\\d:\\d\\d:\\d\\d)\\s*(?=[A-Z][A-Za-z]+:)");
  private static final Pattern DELIM2 = Pattern.compile("\\*(?=Loc:|Address:|Apt:|City:|Parish:)| +(?=State:|Latitude:|Longitude:)");

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
        body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
        body = body.replace("Address:", " Address:").replace(" Apt.", " Apt:");
        return super.parseMsg(body,  data);
      }

      if (body.startsWith("Cancelled or Reassigned ,")) {
        data.msgType = MsgType.RUN_REPORT;
        body = body.replace("Incident listed above has been Cancelled or Reassigned.", ",");
        setSelectValue("CANCEL");
        return parseFields(body.split(" , "), data);
      }

      if (body.startsWith("Address Changed To:")) {
        data.strCall = "Address Change";
        body = body.substring(19).trim();
        body = body.replace(" Apt.", " Apt:");
        setSelectValue("ADDRCH");
      }

      FParser fp = new FParser(body);
      if (fp.check("Updated Priority:")) {
        setFieldList("PLACE ADDR APT CITY ST CODE CALL INFO");
        data.strCall = "Updated Priority";
        if (fp.check("Location:")) data.strPlace = fp.get(400);
        if (!fp.check("Address:")) return false;
        parseAddress(fp.get(400), data);
        fp.check("City:");
        data.strCity = fp.get(35);
        fp.check("State:");
        data.strState = fp.get(5);
        parseCodeCall(fp.get(60), data);
        data.strSupp = fp.get(35);
        return true;
      }

      if (fp.check("Inc Times")) {
        setFieldList("ADDR APT CITY ST INFO");
        data.msgType = MsgType.RUN_REPORT;
        if (!fp.check("Address:")) return false;
        parseAddress(fp.get(400), data);
        if (!fp.check("City:")) return false;
        data.strCity = fp.get(35);
        if (!fp.check("State:")) return false;
        data.strState = fp.get(5);
        fp.skip(20);  // We already have call ID
        data.strSupp = fp.get().replace("\\ ", "\n");
        return true;
      }

      body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
      if (!super.parseMsg(body, data)) return false;
    }
    else if (body.contains("*Loc:")) {
      setSelectValue("1");
      if (!parseFields(DELIM2.split(body), data)) return false;
    }
    else if (body.startsWith("Location:")) {
      setSelectValue("2");
      return super.parseMsg(body, data);
    }
    else return false;

    // Fix some state specific issues
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

  @Override
  public String getProgram() {
    return "ID CALL? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("DEST")) return new MyDestField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      parseCodeCall(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d\\S+)-(.*)");

  private static  void parseCodeCall(String field, Data data) {
    Matcher match = CODE_CALL_PTN.matcher(field);
    if (match.matches()) {
      data.strCode = match.group(1);
      field = match.group(2);
    }
    field = field.replaceAll(" {2,}", " ");
    data.strCall = field;
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("UDC")) field = "San Antonio";
      if (!data.strCity.isEmpty()) return;
      super.parse(field, data);
    }
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");

  private class MyDestField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      FParser fp = new FParser(field);
      if (isBreakAt(field, 80)) {
        data.strSupp = append(data.strSupp, "\n", fp.get(40));
        data.strSupp = append(data.strSupp, "\n", fp.get(30));
        data.strSupp = append(data.strSupp, "  Apt:", fp.get(10));
        data.strSupp = append(data.strSupp, "\n", fp.get());
      } else if (isBreakAt(field, 70)) {
        data.strSupp = append(data.strSupp, "\n", fp.get(30));
        data.strSupp = append(data.strSupp, "\n", MSPACE_PTN.matcher(fp.get(40)).replaceFirst("  Apt:"));
        data.strSupp = append(data.strSupp, "\n", fp.get());
      } else {
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }

    private boolean isBreakAt(String field, int pos) {
      if (field.length() <= pos) return false;
      return field.charAt(pos-1) == ' ' && Character.isUpperCase(field.charAt(pos));
    }
  }
}
