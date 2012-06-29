package uk.ac.standrews.grasp.ide.editors;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.rules.IWordDetector;

//"architecture", "requirement", "quality_attribute", "property",
//"rationale", "reason", "template", "system", "layer", "over",
//"component", "connector", "provides", "requires", "check",
//"link", "extends", "because", "supports", "inhibits"	

enum KeywordDetectors implements IWordDetector {
	ARCHITECTURE("architecture")
	, REQUIREMENT("requirement")
	, QUALITY_ATTRIBUTE("quality_attribute")
	, PROPERTY("property")
	, RATIONALE("rationale")
	, REASON("reason")
	, TEMPLATE("template")
	, SYSTEM("system")
	, LAYER("layer")
	, OVER("over")
	, COMPONENT("component")
	, CONNECTOR("connector")
	, PROVIDES("provides")
	, REQUIRES("requires")
	, CHECK("check")
	, LINK("link")
	, EXTENDS("extends")
	, BECAUSE("because")
	, SUPPORTS("supports")
	, INHIBITS("inhibits")
	;
	
	private String keyword;
	
	private KeywordDetectors(String keyword) {
		Assert.isLegal(keyword != null && keyword.length() > 0);
		this.keyword = keyword;
	}

	@Override
	public boolean isWordStart(char c) {
		return keyword.charAt(0) == c;
	}

	@Override
	public boolean isWordPart(char c) {
		return keyword.indexOf(c) != -1;
	}
}
