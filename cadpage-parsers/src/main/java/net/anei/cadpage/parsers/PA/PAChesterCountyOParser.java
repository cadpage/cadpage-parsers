package net.anei.cadpage.parsers.PA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlDecoder;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyOParser extends PAChesterCountyBaseParser {

  public PAChesterCountyOParser() {
    super("( SELECT/1 Call_Time:DATETIME1/d! EMPTY! Event:ID! Event_Type_Code:CALL! Event_Subtype_Code:CALL/SDS! ESZ:MAP! Beat:BOX! " +
              "Address:EMPTY! PLACE! ADDR! Cross_Street:X! Location_Information:INFO! Development:INFO/N! Municipality:CITY! " +
              "Caller_Information:INFO/N! Caller_Name:NAME! Caller_Phone_Number:PHONE! Alt_Phone_Number:SKIP! Caller_Address:SKIP! " +
              "Caller_Source:SKIP! Units:UNIT! UNIT/S+ Event_Comments:INFO/N+ " +
          "| SELECT/2 SKIP+? Event_ID:EMPTY! ID! Event:EMPTY! ID2! Unit:EMPTY! UNIT! Dispatch_Time:EMPTY! DATETIME2 Event_Type:SKIP! " +
              "Agency:SKIP! Agency:SKIP! Event_Sub-Type:EMPTY! CALL! Dispatch_Group:EMPTY! CH Address:EMPTY! ADDR! Location_Info:EMPTY! PLACE " +
              "Cross_Street:EMPTY! X Municipality:EMPTY! CITY ESZ:EMPTY! MAP Development:EMPTY! MAP/L Beat:EMPTY! MAP/L " +
              "Caller_Name:EMPTY! NAME Caller_Phone:EMPTY! PHONE Caller_Address:EMPTY! SKIP+? Event_Comments%EMPTY INFO2/N+ " +
          "| INCIDENT:CALL! STATUS:SKIP! EVENT_ID:ID! DISPATCH_TIME:DATETIME3! BOX:BOX! DISPATCHED_UNITS:UNIT! Mutual_Aid:UNIT/C? " +
              "LOCATION:EMPTY! ADDR! CITY3! COUNTY_ST3! Common_Place:PLACE3? Development:PLACE3? Misc:PLACE3? X1:X? X2:X? " +
              "Map:GPS3? CALLER_NAME:NAME! CALLER_ADDRESS:SKIP! CALLER_PHONE:PHONE! NOTES:INFO3! INFO3/N+ PORTAL_LINK:URL! " +
          ")");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "gfac55calls@gmail.com,EWFC05@verizon.net,pfdfire@fdcms.info,vfvfco168@comcast.net,westwoodfire@comcast.net,cad@oxfordfire.com,afc23@fdcms.info,whcems@gmail.com,@eastwhitelandfire.org,cadoxfordfire@gmail.com,haacuse96@comcast.net,libertyfc@fdcms.info,@westwoodfire.com,dispatch@diverescue77.org,paging@honeybrookfire.org,@ehbems.org,whcems@outlook.com,calls@goodfellowship.org,@fdcmsincidents.com,@fdcms3.info,@westend67.com,Oxford_Union_Fire_Company@alerts.stationcad.com,nfdpc25norco@norcofireco.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static HtmlDecoder decoder = new HtmlDecoder();

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    int pt = body.indexOf("<style>\n");
    if (pt >= 0) body = body.substring(pt);
    if (body.startsWith("<style>")) {
      setSelectValue("2");
      String[] flds = decoder.parseHtml(body);
      if (flds == null) return false;
      if (flds[flds.length-1].endsWith("**")) {
        flds[flds.length-1] = "";
      }
      return parseFields(flds, data);
    }
    else {
      setSelectValue("1");
      body = stripFieldEnd(body, "**");
      return super.parseHtmlMsg(subject, body, data);
    }
  }

  private static final String MARKER_TEXT = "Chester County Emergency Services Dispatch Report";
  private static final String MARKER = MARKER_TEXT + " \n\n";

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf(MARKER);
    if (pt >= 0) {
      setSelectValue("1");
      body = body.substring(pt+MARKER.length()).trim();
    } else {
      setSelectValue("3");
    }
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME1")) return new DateTimeField("\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d|", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("MARKER")) return new SkipField(MARKER_TEXT, true);
    if (name.equals("ID2")) return new MyId2Field();
    if (name.equals("DATETIME2")) return new MyDateTime2Field();
    if (name.equals("INFO2")) return new MyInfo2Field();
    if (name.equals("DATETIME3")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("CITY3")) return new MyCity3Field();
    if (name.equals("COUNTY_ST3")) return new MyCountyState3Field();
    if (name.equals("PLACE3")) return new MyPlace3Field();
    if (name.equals("GPS3")) return new MyGPS3Field();
    if (name.equals("INFO3")) return new MyInfo3Field();
    return super.getField(name);
  }

  private class MyAddressField extends A7AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('\n');
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE? " + super.getFieldNames();
    }
  }

  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("x Type:");
      if (pt >= 0) {
        field = append(field.substring(0,pt).trim(), " X:", field.substring(pt+7).trim());
      }
      super.parse(field, data);
    }
  }

  private class MyId2Field extends IdField {
    @Override
    public void parse(String field, Data data) {
      data.strCallId = append(field, "/", data.strCallId);
    }
  }

  private static final Pattern DATE_TIME2_PTN = Pattern.compile("(\\d\\d)-(\\d\\d)-(\\d\\d) (\\d\\d?:\\d\\d:\\d\\d)(?: ED)?");
  private class MyDateTime2Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME2_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(1)+'/'+match.group(3);
      data.strTime = match.group(4);
    }
  }

  private static final Pattern INFO_JUNK2_PTN = Pattern.compile("\\d\\d?:\\d\\d:\\d\\d|[a-z]+\\d+");
  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK2_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }

  private class MyCity3Field extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt == 0) {
        field = stripFieldEnd(field.substring(1), ")");
      } else if (pt > 0) {
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
  }

  private class MyCountyState3Field extends Field {

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "(");
      field = stripFieldEnd(field, ")");
      String[] parts = field.split(",");
      int last = parts.length-1;
      String state = parts[last--].trim();
      if (state.equalsIgnoreCase("Pennsylvania")) {
        data.strState = "PA";
      } else if (state.equalsIgnoreCase("Maryland")) {
        data.strState = "MD";
      } else if (state.equalsIgnoreCase("Delaware")) {
        data.strState = "DE";
      } else {
        last++;
      }
      if (data.strCity.isEmpty() && last >= 0) data.strCity = parts[0].trim();
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }

  private class MyPlace3Field extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (!field.isEmpty()) data.strPlace = field;
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("\\+USA/@(\\d{2}\\.\\d{6,7},-\\d{2}\\.\\d{6,7})");
  private class MyGPS3Field extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.find()) setGPSLoc(match.group(1), data);
    }
  }

  private static final Pattern INFO_JUNK3_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d ~ \\S+ - +");
  private class MyInfo3Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_JUNK3_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "1680 EB RT 422",                       "+40.243467,-75.698166",
      "1681 EB RT 422",                       "+40.243467,-75.698166",
      "1682 EB RT 422",                       "+40.243467,-75.698166",
      "1683 EB RT 422",                       "+40.243467,-75.698166",
      "1684 EB RT 422",                       "+40.243467,-75.698166",
      "1685 EB RT 422",                       "+40.243467,-75.698166",
      "1686 EB RT 422",                       "+40.243467,-75.698166",
      "1687 EB RT 422",                       "+40.243467,-75.698166",
      "1688 EB RT 422",                       "+40.243467,-75.698166",
      "1689 EB RT 422",                       "+40.243467,-75.698166",
      "1690 EB RT 422",                       "+40.243467,-75.698166",
      "1691 EB RT 422",                       "+40.243467,-75.698166",
      "1692 EB RT 422",                       "+40.243467,-75.698166",
      "1693 EB RT 422",                       "+40.243467,-75.698166",
      "1694 EB RT 422",                       "+40.243467,-75.698166",
      "1695 EB RT 422",                       "+40.243467,-75.698166",
      "1696 EB RT 422",                       "+40.243467,-75.698166",
      "1697 EB RT 422",                       "+40.243467,-75.698166",
      "1698 EB RT 422",                       "+40.243467,-75.698166",
      "1699 EB RT 422",                       "+40.243467,-75.698166",
      "1700 EB RT 422",                       "+40.239582,-75.685995",
      "1701 EB RT 422",                       "+40.239875,-75.685198",
      "1702 EB RT 422",                       "+40.243120,-75.678622",
      "1703 EB RT 422",                       "+40.243120,-75.678622",
      "1704 EB RT 422",                       "+40.243120,-75.678622",
      "1705 EB RT 422",                       "+40.243120,-75.678622",
      "1706 EB RT 422",                       "+40.243195,-75.676970",
      "1707 EB RT 422",                       "+40.242957,-75.675240",
      "1708 EB RT 422",                       "+40.242696,-75.673550",
      "1709 EB RT 422",                       "+40.241613,-75.666830",
      "1710 EB RT 422",                       "+40.241613,-75.666830",
      "1711 EB RT 422",                       "+40.241613,-75.666830",
      "1712 EB RT 422",                       "+40.241613,-75.666830",
      "1713 EB RT 422",                       "+40.241613,-75.666830",
      "1714 EB RT 422",                       "+40.241613,-75.666830",
      "1715 EB RT 422",                       "+40.239982,-75.660849",
      "1716 EB RT 422",                       "+40.239442,-75.659097",
      "1717 EB RT 422",                       "+40.238909,-75.657085",
      "1718 EB RT 422",                       "+40.238520,-75.655097",
      "1719 EB RT 422",                       "+40.238343,-75.654174",
      "1720 EB RT 422",                       "+40.237781,-75.651304",
      "1721 EB RT 422",                       "+40.234772,-75.642477",
      "1722 EB RT 422",                       "+40.234772,-75.642477",
      "1723 EB RT 422",                       "+40.234772,-75.642477",
      "1724 EB RT 422",                       "+40.234772,-75.642477",
      "1725 EB RT 422",                       "+40.234772,-75.642477",
      "1726 EB RT 422",                       "+40.234772,-75.642477",
      "1727 EB RT 422",                       "+40.233638,-75.638713",
      "1728 EB RT 422",                       "+40.233425,-75.637833",
      "1729 EB RT 422",                       "+40.232930,-75.635913",
      "1730 EB RT 422",                       "+40.232483,-75.634135",
      "1731 EB RT 422",                       "+40.232055,-75.632438",
      "1732 EB RT 422",                       "+40.231526,-75.630316",
      "1733 EB RT 422",                       "+40.231185,-75.628478",
      "1734 EB RT 422",                       "+40.231252,-75.627397",
      "1735 EB RT 422",                       "+40.231617,-75.625068",
      "1736 EB RT 422",                       "+40.232025,-75.623107",
      "1737 EB RT 422",                       "+40.232409,-75.621272",
      "1738 EB RT 422",                       "+40.232872,-75.618925",
      "1739 EB RT 422",                       "+40.233246,-75.617725",
      "1740 EB RT 422",                       "+40.234281,-75.615851",
      "1741 EB RT 422",                       "+40.235189,-75.614807",
      "1742 EB RT 422",                       "+40.236272,-75.613615",
      "1743 EB RT 422",                       "+40.240088,-75.608729",
      "1744 EB RT 422",                       "+40.240088,-75.608729",
      "1745 EB RT 422",                       "+40.240088,-75.608729",
      "1746 EB RT 422",                       "+40.240088,-75.608729",
      "1747 EB RT 422",                       "+40.240088,-75.608729",
      "1748 EB RT 422",                       "+40.240984,-75.599030",
      "1749 EB RT 422",                       "+40.240984,-75.599030",
      "1750 EB RT 422",                       "+40.240984,-75.599030",
      "1751 EB RT 422",                       "+40.240984,-75.599030",
      "1752 EB RT 422",                       "+40.240984,-75.599030",
      "1753 EB RT 422",                       "+40.240984,-75.599030",
      "1754 EB RT 422",                       "+40.242386,-75.590972",
      "1755 EB RT 422",                       "+40.242386,-75.590972",
      "1756 EB RT 422",                       "+40.242386,-75.590972",
      "1757 EB RT 422",                       "+40.242386,-75.590972",
      "1758 EB RT 422",                       "+40.242738,-75.585029",
      "1759 EB RT 422",                       "+40.242738,-75.585029",
      "1760 EB RT 422",                       "+40.242738,-75.585029",
      "1761 EB RT 422",                       "+40.242738,-75.585029",
      "1762 EB RT 422",                       "+40.241865,-75.579140",
      "1763 EB RT 422",                       "+40.241491,-75.576705",
      "1764 EB RT 422",                       "+40.241179,-75.575582",
      "1765 EB RT 422",                       "+40.232282,-75.558309",
      "1766 EB RT 422",                       "+40.232282,-75.558309",
      "1767 EB RT 422",                       "+40.232282,-75.558309",
      "1768 EB RT 422",                       "+40.232282,-75.558309",
      "1769 EB RT 422",                       "+40.232282,-75.558309",
      "1770 EB RT 422",                       "+40.232282,-75.558309",
      "1771 EB RT 422",                       "+40.232282,-75.558309",
      "1772 EB RT 422",                       "+40.232282,-75.558309",
      "1773 EB RT 422",                       "+40.232282,-75.558309",
      "1774 EB RT 422",                       "+40.232282,-75.558309",
      "1775 EB RT 422",                       "+40.232282,-75.558309",
      "1776 EB RT 422",                       "+40.232282,-75.558309",
      "1777 EB RT 422",                       "+40.232282,-75.558309",
      "1778 EB RT 422",                       "+40.232282,-75.558309",
      "1779 EB RT 422",                       "+40.232282,-75.558309",
      "1780 EB RT 422",                       "+40.232282,-75.558309",
      "1781 EB RT 422",                       "+40.232282,-75.558309",
      "1782 EB RT 422",                       "+40.232282,-75.558309",
      "1783 EB RT 422",                       "+40.232282,-75.558309",
      "1784 EB RT 422",                       "+40.232282,-75.558309",
      "1785 EB RT 422",                       "+40.232282,-75.558309",
      "1786 EB RT 422",                       "+40.232282,-75.558309",
      "EB RT 422 TO EB SCHUYLKILL RD",        "+40.230961,-75.628792",
      "EB RT 422 TO NB RT 100",               "+40.238608,-75.657655",
      "EB RT 422 TO SB KIEM ST",              "+40.232910,-75.636862",
      "EB RT 422 TO SB RT 100",               "+40.239507,-75.660235",
      "EB RT 724 TO NB RT 100",               "+40.234185,-75.659796",
      "EB SCHUYLKILL RD TO EB RT 422",        "+40.231020,-75.627455",
      "EB SCHUYLKILL RD TO WB RT 422",        "+40.231989,-75.630030",
      "NB RT 100 TO EB RT 422",               "+40.237979,-75.658417",
      "NB RT 100 TO WB RT 422",               "+40.240050,-75.658512",
      "NB RT 100 TO WB RT 724",               "+40.232316,-75.659848",
      "RAMP ARMAND HAMMER TO RT422",          "+40.233479,-75.618026",
      "RAMP RT422 EB TO ARMAND HAMMER BLVD",  "+40.232859,-75.617827",
      "RAMP RT422 EB TO S GROSSTOWN RD",      "+40.238798,-75.689081",
      "RAMP RT422 TO S GROSSTOWN RD",         "+40.239646,-75.689095",
      "RAMP RT422 WB TO S GROSSTOWN RD",      "+40.239646,-75.689095",
      "RAMP S GROSSTOWN RD TO RT422",         "+40.240134,-75.692044",
      "RT422 WB TO ARMAND HAMMER BLVD",       "+40.234508,-75.616480",
      "SB HANOVER ST TO EB RT 422",           "+40.237331,-75.653397",
      "SB KEIM ST TO WB RT 422",              "+40.233459,-75.636366",
      "SB RT 100 TO EB RT 422",               "+40.238648,-75.659529",
      "SB RT 100 TO EB RT 724",               "+40.232501,-75.660842",
      "SB RT 100 TO WB RT 422",               "+40.240398,-75.660561",
      "SB RT 100 TO WB RT 724",               "+40.233800,-75.660668",
      "3550 SCHUYLKILL RIVER TRL",            "+40.199645,-75.583331",
      "3575 SCHUYLKILL RIVER TRL",            "+40.202486,-75.585650",
      "3600 SCHUYLKILL RIVER TRL",            "+40.205667,-75.588213",
      "3625 SCHUYLKILL RIVER TRL",            "+40.205559,-75.588130",
      "3650 SCHUYLKILL RIVER TRL",            "+40.211845,-75.593101",
      "3675 SCHUYLKILL RIVER TRL",            "+40.215387,-75.594673",
      "3700 SCHUYLKILL RIVER TRL",            "+40.218892,-75.595867",
      "3725 SCHUYLKILL RIVER TRL",            "+40.222263,-75.597016",
      "3750 SCHUYLKILL RIVER TRL",            "+40.224719,-75.599383",
      "3775 SCHUYLKILL RIVER TRL",            "+40.225365,-75.603487",
      "3800 SCHUYLKILL RIVER TRL",            "+40.223313,-75.607240",
      "3825 SCHUYLKILL RIVER TRL",            "+40.222555,-75.610747",
      "3850 SCHUYLKILL RIVER TRL",            "+40.223564,-75.616414",
      "3875 SCHUYLKILL RIVER TRL",            "+40.225682,-75.620298",
      "3900 SCHUYLKILL RIVER TRL",            "+40.227986,-75.623303",
      "3925 SCHUYLKILL RIVER TRL",            "+40.229800,-75.625225",
      "3950 SCHUYLKILL RIVER TRL",            "+40.231231,-75.626447",
      "1680 WB RT 422",                       "+40.246229,-75.705855",
      "1681 WB RT 422",                       "+40.246229,-75.705855",
      "1682 WB RT 422",                       "+40.246229,-75.705855",
      "1683 WB RT 422",                       "+40.246229,-75.705855",
      "1684 WB RT 422",                       "+40.246229,-75.705855",
      "1685 WB RT 422",                       "+40.246229,-75.705855",
      "1686 WB RT 422",                       "+40.246229,-75.705855",
      "1687 WB RT 422",                       "+40.246229,-75.705855",
      "1688 WB RT 422",                       "+40.246229,-75.705855",
      "1689 WB RT 422",                       "+40.246229,-75.705855",
      "1690 WB RT 422",                       "+40.246229,-75.705855",
      "1691 WB RT 422",                       "+40.246229,-75.705855",
      "1692 WB RT 422",                       "+40.246229,-75.705855",
      "1693 WB RT 422",                       "+40.246229,-75.705855",
      "1694 WB RT 422",                       "+40.246229,-75.705855",
      "1695 WB RT 422",                       "+40.246229,-75.705855",
      "1696 WB RT 422",                       "+40.246229,-75.705855",
      "1697 WB RT 422",                       "+40.240024,-75.692309",
      "1698 WB RT 422",                       "+40.239572,-75.690907",
      "1699 WB RT 422",                       "+40.239315,-75.688988",
      "1700 WB RT 422",                       "+40.239395,-75.687451",
      "1701 WB RT 422",                       "+40.239814,-75.685728",
      "1702 WB RT 422",                       "+40.242250,-75.681825",
      "1703 WB RT 422",                       "+40.242250,-75.681825",
      "1704 WB RT 422",                       "+40.242250,-75.681825",
      "1705 WB RT 422",                       "+40.242250,-75.681825",
      "1706 WB RT 422",                       "+40.243329,-75.677353",
      "1707 WB RT 422",                       "+40.243178,-75.675700",
      "1708 WB RT 422",                       "+40.242844,-75.673657",
      "1709 WB RT 422",                       "+40.242291,-75.670115",
      "1710 WB RT 422",                       "+40.242291,-75.670115",
      "1711 WB RT 422",                       "+40.242291,-75.670115",
      "1712 WB RT 422",                       "+40.242291,-75.670115",
      "1713 WB RT 422",                       "+40.241220,-75.664605",
      "1714 WB RT 422",                       "+40.240384,-75.661666",
      "1715 WB RT 422",                       "+40.240010,-75.660449",
      "1716 WB RT 422",                       "+40.239684,-75.659320",
      "1717 WB RT 422",                       "+40.239030,-75.657021",
      "1718 WB RT 422",                       "+40.238754,-75.655693",
      "1719 WB RT 422",                       "+40.238403,-75.653712",
      "1720 WB RT 422",                       "+40.237960,-75.651361",
      "1721 WB RT 422",                       "+40.237528,-75.649803",
      "1722 WB RT 422",                       "+40.237169,-75.648633",
      "1723 WB RT 422",                       "+40.236007,-75.645116",
      "1724 WB RT 422",                       "+40.236007,-75.645116",
      "1725 WB RT 422",                       "+40.236007,-75.645116",
      "1726 WB RT 422",                       "+40.236007,-75.645116",
      "1727 WB RT 422",                       "+40.234045,-75.639748",
      "1728 WB RT 422",                       "+40.233641,-75.637622",
      "1729 WB RT 422",                       "+40.233126,-75.636121",
      "1730 WB RT 422",                       "+40.232647,-75.634249",
      "1731 WB RT 422",                       "+40.232243,-75.632656",
      "1732 WB RT 422",                       "+40.231666,-75.630385",
      "1733 WB RT 422",                       "+40.231361,-75.629026",
      "1734 WB RT 422",                       "+40.231418,-75.627133",
      "1735 WB RT 422",                       "+40.231763,-75.625191",
      "1736 WB RT 422",                       "+40.232139,-75.623391",
      "1737 WB RT 422",                       "+40.232447,-75.621721",
      "1738 WB RT 422",                       "+40.232978,-75.619535",
      "1739 WB RT 422",                       "+40.233340,-75.617883",
      "1740 WB RT 422",                       "+40.233872,-75.616718",
      "1741 WB RT 422",                       "+40.235160,-75.615040",
      "1742 WB RT 422",                       "+40.237435,-75.612530",
      "1743 WB RT 422",                       "+40.237435,-75.612530",
      "1744 WB RT 422",                       "+40.237435,-75.612530",
      "1745 WB RT 422",                       "+40.240051,-75.609150",
      "1746 WB RT 422",                       "+40.240051,-75.609150",
      "1747 WB RT 422",                       "+40.240051,-75.609150",
      "1748 WB RT 422",                       "+40.240051,-75.609150",
      "1749 WB RT 422",                       "+40.241111,-75.599563",
      "1750 WB RT 422",                       "+40.241111,-75.599563",
      "1751 WB RT 422",                       "+40.241111,-75.599563",
      "1752 WB RT 422",                       "+40.241111,-75.599563",
      "1753 WB RT 422",                       "+40.241521,-75.593455",
      "1754 WB RT 422",                       "+40.241521,-75.593455",
      "1755 WB RT 422",                       "+40.241521,-75.593455",
      "1756 WB RT 422",                       "+40.243127,-75.586979",
      "1757 WB RT 422",                       "+40.243127,-75.586979",
      "1758 WB RT 422",                       "+40.243127,-75.586979",
      "1759 WB RT 422",                       "+40.243127,-75.586979",
      "1760 WB RT 422",                       "+40.243127,-75.586979",
      "1761 WB RT 422",                       "+40.242389,-75.581812",
      "1762 WB RT 422",                       "+40.242042,-75.578858",
      "1763 WB RT 422",                       "+40.241642,-75.576508",
      "1764 WB RT 422",                       "+40.241290,-75.574555",
      "1765 WB RT 422",                       "+40.235687,-75.563936",
      "1766 WB RT 422",                       "+40.235687,-75.563936",
      "1767 WB RT 422",                       "+40.235687,-75.563936",
      "1768 WB RT 422",                       "+40.235687,-75.563936",
      "1769 WB RT 422",                       "+40.235687,-75.563936",
      "1770 WB RT 422",                       "+40.235687,-75.563936",
      "1771 WB RT 422",                       "+40.235687,-75.563936",
      "1772 WB RT 422",                       "+40.235687,-75.563936",
      "1773 WB RT 422",                       "+40.235687,-75.563936",
      "1774 WB RT 422",                       "+40.235687,-75.563936",
      "1775 WB RT 422",                       "+40.235687,-75.563936",
      "1776 WB RT 422",                       "+40.235687,-75.563936",
      "1777 WB RT 422",                       "+40.235687,-75.563936",
      "1778 WB RT 422",                       "+40.235687,-75.563936",
      "1779 WB RT 422",                       "+40.235687,-75.563936",
      "1780 WB RT 422",                       "+40.235687,-75.563936",
      "1781 WB RT 422",                       "+40.235687,-75.563936",
      "1782 WB RT 422",                       "+40.235687,-75.563936",
      "1783 WB RT 422",                       "+40.235687,-75.563936",
      "1784 WB RT 422",                       "+40.235687,-75.563936",
      "1785 WB RT 422",                       "+40.235687,-75.563936",
      "WB RT 422 TO EB SCHUYLKILL RD",        "+40.231799,-75.629570",
      "WB RT 422 TO NB RT 100",               "+40.239393,-75.656789",
      "WB RT 422 TO SB HANOVER ST",           "+40.238040,-75.650731",
      "WB RT 422 TO SB RT 100",               "+40.240266,-75.660164",
      "WB RT 724 TO NB RT 100",               "+40.233640,-75.659538",
      "WB RT 724 TO SB RT 100",               "+40.232996,-75.662906"
  });
}
