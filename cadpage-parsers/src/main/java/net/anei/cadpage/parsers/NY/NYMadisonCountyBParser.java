package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA13Parser;


public class NYMadisonCountyBParser extends DispatchA13Parser {

  private static final Pattern MISMATCH_PAREN_PTN = Pattern.compile("(\\([^\\)]*)(?=\\()");

  public NYMadisonCountyBParser() {
    super(NYMadisonCountyParser.CITY_LIST, "MADISON COUNTY", "NY", A13_FLG_LEAD_PLACE_NAME);
    removeWords("COUNTY");
  }

  @Override
  public String getFilter() {
    return "e-911@co.madison.ny.us,e911@madisoncounty.ny.go,messaging@iamresponding.com,e911@bounce.secureserver.net,e911@madisoncounty.ny.gov";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    body = body.trim();
    if (body.startsWith("<!DOCTYPE HTML ")) {
      int pt = body.indexOf("<p>=EF=BB=BF");
      if (pt < 0) return false;
      pt += 12;
      int pt2 = body.indexOf("</p>", pt);
      if (pt2 < 0) return false;
      body = body.substring(pt, pt2).trim();
    }
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Missed right parens cause a problem.  If we find any add a closing right paren.
    body = MISMATCH_PAREN_PTN.matcher(body).replaceAll("$1)");

    if (subject.equals("SEVAC")) {
      data.strSource = subject;
      body = body.replace("\n\n", "\n");
      if (!body.contains("Dispatched") && !body.contains("Standby")) body = "Dispatched\n" + body;
      body = body.replace("=20\n", "");
    }

    else if (subject.equals("Greater Lenox")) {
      body = body.replace("\n\n", "\n");
    }

    if (!super.parseMsg(body, data)) return false;

    data.strCity = stripFieldEnd(data.strCity, " VIL");
    data.strCity = stripFieldEnd(data.strCity," VILLAGE");
    return true;

  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("MORRISVILLE VILLAGE-SUNY")) {
      city = "MORRISVILLE";
    }
    return city;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static final Pattern OOC_ADDR_PTN = Pattern.compile("([ A-Z]+ COUNTY), \\((.*)\\) ; (.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {

      Matcher match = OOC_ADDR_PTN.matcher(field);
      if (match.matches()) {
        data.strCity = match.group(1).trim();
        data.strCross = match.group(2).trim();
        String addr = match.group(3).trim();
        int pt = addr.lastIndexOf(',');
        if (pt >= 0) {
          data.strCity = addr.substring(pt+1).trim();
          addr = addr.substring(0,pt).trim();
        }
        parseAddress(addr, data);
        return;
      }

      if (field.startsWith("@") || field.contains("(")) {
        super.parse(field, data);
      } else {
        String place = "";
        int pt = field.lastIndexOf(';');
        if (pt >= 0) {
          place = field.substring(pt+1).trim();
          field = field.substring(0,pt).trim();
          if (field.startsWith(place)) place = "";
        }
        parseAddress(StartType.START_PLACE, FLAG_CROSS_FOLLOWS, field, data);
        data.strCross = getLeft();
        data.strCross = stripFieldStart(data.strCross, "/");
        data.strCross = stripFieldEnd(data.strCross, "/");

        if (data.strPlace.length() > 0 && data.strCity.length() == 0) {
          parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, data.strPlace, data);
          data.strPlace = getStart();
        }
        data.strPlace = append(data.strPlace, " - ", place);
        data.strCross = stripFieldEnd(data.strCross, data.strCity);
      }
    }
  }

}
