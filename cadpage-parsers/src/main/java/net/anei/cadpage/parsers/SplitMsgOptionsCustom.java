package net.anei.cadpage.parsers;

public class SplitMsgOptionsCustom implements SplitMsgOptions {

  @Override public boolean splitDirectPage() { return true; }

  @Override public int splitMinMsg() { return 0; }

  @Override public boolean splitBreakIns() { return false; }

  @Override public boolean splitBlankIns() { return true; }

  @Override public boolean splitChkSender() { return true; }

  @Override public boolean splitKeepLeadBreak() { return false; }

  @Override public boolean revMsgOrder() { return false;   }

  @Override public boolean mixedMsgOrder() { return false; }

  @Override public boolean noParseSubjectFollow() { return false; }

  @Override public int splitBreakLength() { return 0; }

  @Override public int splitBreakPad() { return 0; }

  @Override public boolean subjectColonField() { return false; }

  @Override public boolean insConditionalBreak() { return false; }
}
