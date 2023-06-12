 package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class COBoulderCountyBParser extends FieldProgramParser {

	public COBoulderCountyBParser() {
		super("BOULDER COUNTY", "CO",
		      "( Comment:INFO CAD:ID CALL:CALL LOC:PLACE ADD:ADDR CITY:CITY INFO:INFO/N UNIT:UNIT " +
		      "| ADDR:ADDR APT:APT! PROB:PROB! UNITS:UNIT! Map_Page:MAP! Assigned_Units:UNIT " +
	        "| CALL! ADD:ADDR! BLD:APT! APT:APT! LOC:PLACE! INFO:INFO! TIME:TIME! UNITS:UNIT% )");
	}

	@Override
	public String getFilter() {
	  return "bretsa@bretsaps.org";
	}

	@Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
	  return new SplitMsgOptionsCustom(){
	    @Override public boolean splitBlankIns() { return false; }
	    @Override public boolean mixedMsgOrder() { return true; }
	    @Override public int splitBreakLength() { return 150; }
	  };
  }

	private static Pattern MASTER1 = Pattern.compile("\\*{3} ADVISORY NOTIFICATION \\*{3}Inc #([A-Z]{3,4})(\\d{6}-\\d{6})Type:z?(.*)Addr:(.*)Unit:(.*)");
	private static Pattern DELIM2 = Pattern.compile(" *(?=(?:CAD|CALL|LOC|ADD|CITY|INFO|UNIT):)");
  private static Pattern SRC_ID = Pattern.compile("([A-Z]{3,4})(\\d{6}-\\d{6}) +(.*)");
	private static Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(ADD|APT|BLD|INFO|LOC|PROB|TIME|UNITS|Map Page):");

	public boolean parseMsg(String body, Data data) {

	  Matcher mat = MASTER1.matcher(body);
	  if (mat.matches()) {
	    setFieldList("SRC ID CALL ADDR APT UNIT");
	    data.strSource = mat.group(1);
	    data.strCallId = mat.group(2);
	    data.strCall = mat.group(3).trim();
	    parseAddress(mat.group(4).trim(), data);
	    data.strUnit = mat.group(5).trim();
	    return true;
	  }

	  if (body.startsWith("Comment:")) {
	    return parseFields(DELIM2.split(body), data);
	  }

	  mat = SRC_ID.matcher(body);
	  if (!mat.matches()) return false;
	  data.strSource = mat.group(1);
	  data.strCallId = mat.group(2);
	  body = mat.group(3);

	  body = body.replace("Response:", "UNITS:");
	  body = MISSING_BLANK_PTN.matcher(body).replaceAll(" $1:");

	  return super.parseMsg(body, data);
	}

  @Override
  public String getProgram() {
    return "SRC ID "+super.getProgram();
  }

	@Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("PROB")) return new MyProbField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d");
    return super.getField(name);
  }

	private class MyCityField extends CityField {
	  @Override
	  public void parse(String field, Data data) {
	    field = stripFieldEnd(field, " U");
	    super.parse(field, data);
	  }
	}

	private static Pattern PROB = Pattern.compile("(.*) \\((L\\d)\\)(?: ([\\dA-Z]+))?");
	private class MyProbField extends CallField {
	    @Override
	    public void parse(String field, Data data) {
	      Matcher mat = PROB.matcher(field);
	      if (!mat.matches()) super.parse(field, data);
	      else {
	        data.strCall = mat.group(1).trim();
	        data.strPriority = mat.group(2);
	        data.strCode = getOptGroup(mat.group(3));
	      }
	    }

	    @Override
	    public String getFieldNames() {
	      return "CALL PRI CODE";
	    }
	  }

}
