package com.sarveshparab.analysers.smells;



import com.sarveshparab.util.FileHandler;
import com.sarveshparab.util.XMLUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.xpath.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class SmellAnalyser {


    private Set<String> smellyFiles = new HashSet<>();
    private Map<String,Set<String>> smellFileMap = new HashMap<>();
    private Map<String,Set<String>> fileSmellMap = new HashMap<>();

    public void runSmellAnalyser(String filePath) {

        Document doc = readXML(filePath);

        Node parent = doc.getFirstChild();
        NodeList childNodes = parent.getChildNodes();


        for(int i =0;i<childNodes.getLength();i++){
            Node smellStructure = childNodes.item(i);

            if (smellStructure.getNodeType() == Node.ELEMENT_NODE) {
                // get smell type
                String smellType = smellStructure.getNodeName().substring(smellStructure.getNodeName().lastIndexOf('.') + 1);
                System.out.println("Smell Type " +smellType);

                // find smelly files in that smell
                Set<String> files = getSmellyFilesFromSmellType(smellStructure);

                // make or add file smell map
                for(String file : files){
                    Set<String> smells = fileSmellMap.getOrDefault(file,new HashSet<>());
                    smells.add(smellType);
                    fileSmellMap.put(file,smells);

                }
                // make or add to smell file map

                Set<String> particularSmellFiles = smellFileMap.getOrDefault(smellType,new HashSet<>());
                particularSmellFiles.addAll(files);
                smellFileMap.put(smellType,particularSmellFiles);

                // add to list of smelly files

                smellyFiles.addAll(files);

            }

        }

    }



    private Set<String> getSmellyFilesFromSmellType(Node smellStructure){

        Set<String> files = new HashSet<>();


        Element smellStructureElement = (Element) smellStructure;
        NodeList clusterNodes = smellStructureElement.getElementsByTagName("clusters");

        for(int i =0;i<clusterNodes.getLength();i++){

            Element element = (Element) clusterNodes.item(i);

            NodeList nodes =  element.getChildNodes();
            for(int z = 0;z<nodes.getLength();z++){
                if (nodes.item(z).getNodeType() == Node.ELEMENT_NODE) {

                    String referencePresent = ((Element) nodes.item(z)).getAttribute("reference");

                    System.out.println(((Element) nodes.item(z)).getAttribute("reference"));

                    if(referencePresent.trim().length() !=0){


                        referencePresent = referencePresent.replace("../../../","//");
                        // go to that reference
                        XPath xPath =  XPathFactory.newInstance().newXPath();

                        try {
                            XPathExpression expr =  xPath.compile(referencePresent.trim());
                            Object result = expr.evaluate(element, XPathConstants.NODE);
                            Element newNode = (Element) result;
                            System.out.println(" get from reference");
                            files.addAll(getFiles(newNode));

                        } catch (XPathExpressionException e) {
                            System.out.println(" ERROR!!!!");
                            e.printStackTrace();
                        }


                    }

                    else {
                        // pick up smelly files
                        System.out.println(" get from normal");
                        files.addAll(getFiles(element));
                    }

                }

            }

        }

        return files;
    }




    private Set<String> getFiles(Element element) {

        Set<String> files = new HashSet<>();

        NodeList fileNames = element.getElementsByTagName("string");

        for (int j = 0; j < fileNames.getLength(); j++) {
            Element element1 = (Element) fileNames.item(j);
            // System.out.println(element1.getTextContent());
            // Removing data after $ to only get the file name
            String smellyFile;
            //String smellyFile = element1.getTextContent().split("$")[0];
            if (element1.getTextContent().contains("$")) {
                smellyFile = element1.getTextContent().substring(0, element1.getTextContent().indexOf("$"));
            } else {
                smellyFile = element1.getTextContent();
            }
            // System.out.println(smellyFile);
            files.add(smellyFile);
        }

        return files;
    }




    private static Document readXML(String filePath){

        String contents = FileHandler.readLineByLine(filePath);
        Document doc = XMLUtil.convertStringToXMLDocument(contents);

        return doc;
    }



    public Set<String> getSmellyFiles() {
        return smellyFiles;
    }

    public void setSmellyFiles(Set<String> smellyFiles) {
        this.smellyFiles = smellyFiles;
    }



    public Map<String, Set<String>> getFileSmellMap() {
        return fileSmellMap;
    }

    public void setFileSmellMap(Map<String, Set<String>> fileSmellMap) {
        this.fileSmellMap = fileSmellMap;
    }

    public Map<String, Set<String>> getSmellFileMap() {
        return smellFileMap;
    }

    public void setSmellFileMap(Map<String, Set<String>> smellFileMap) {
        this.smellFileMap = smellFileMap;
    }
}
