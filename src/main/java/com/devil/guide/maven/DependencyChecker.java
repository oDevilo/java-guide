package com.devil.guide.maven;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.limbo.utils.JacksonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 执行在各个模块生成对应的依赖树文件
 * mvn dependency:tree -Doutput=tree.txt
 * <p>
 * 配置config文件
 * [
 * "groupId:artifactId>=version",
 * "groupId:artifactId>=version"
 * ]
 *
 * @author Devil
 * @since 2021/11/29
 */
public class DependencyChecker {

    private final String projectPath;
    private List<String> versions;
    /**
     * 是否打印依赖树
     */
    private final boolean showDependencyTree;
    /**
     * 不满足的版本
     */
    private int dissatisfyVersionNum;
    /**
     * 冲突数
     */
    private int conflictNum;

    public DependencyChecker(String projectPath, String configPath, boolean showDependencyTree) throws Exception {
        this.projectPath = projectPath;
        this.showDependencyTree = showDependencyTree;

        String versionString = IOUtils.toString(new FileInputStream(configPath), Charset.defaultCharset());
        this.versions = JacksonUtils.parseObject(versionString, new TypeReference<List<String>>() {
        });
    }

    public void check() throws Exception {
        doCheckModel(projectPath);

        System.out.println("dissatisfyVersionNum:" + dissatisfyVersionNum);
        System.out.println("conflictNum:" + conflictNum);
    }

    public void doCheckModel(String path) throws Exception {
        List<String> lines = loadDependencyTree(path);
        Xpp3Dom modelDom = Xpp3DomBuilder.build(ReaderFactory.newXmlReader(new File(path + "/pom.xml")));

        // 打印模块名称
        System.out.println("analyzer model: " + modelDom.getChild("artifactId").getValue());

        // 判断是否需要打印依赖树
        if (showDependencyTree) {
            for (String line : lines) {
                System.out.println(line);
            }
        }

        // 根据配置是否存在不满足版本的情况
        for (String version : versions) {
            checkGreaterThan(version, lines);
        }

        // 是否存在多个版本的包
        checkConflicts(lines);

        System.out.println("\n\n\n\n\n");

        // 判断此模块是否为 pom 类型 如果是的话 判断是否还有子模块
        for (Xpp3Dom child : modelDom.getChildren()) {
            if ("packaging".equals(child.getName()) && "pom".equals(child.getValue())) {
                doSubModels(path, modelDom);
            }
        }
    }

    private void checkGreaterThan(String version, List<String> lines) {
        System.out.println("check dependency version: " + version);
        String[] versionSplit = version.split(">=");
        String[] model = versionSplit[0].trim().split(":");
        String v = versionSplit[1].trim();

        Dependency neededDependency = new Dependency(model[0], model[1], v);

        for (String line : lines) {
            if (!line.contains(neededDependency.getArtifactId()) || !line.contains(neededDependency.getGroupId())) {
                continue;
            }
            Dependency modelDependency = getDependencyFromTreeLine(line);
            String msg = "find version: " + modelDependency.getVersion();
            if (modelDependency.greaterThan(neededDependency) < 0) {
                msg += " dissatisfy in line: " + line;
                dissatisfyVersionNum++;
            }
            System.out.println(msg);
        }
    }

    private void checkConflicts(List<String> lines) {
        Map<String, Set<String>> dependencyMap = new HashMap<>();
        for (String line : lines) {
            Dependency modelDependency = getDependencyFromTreeLine(line);
            String name = modelDependency.getGroupId() + ":" + modelDependency.getArtifactId();
            dependencyMap.putIfAbsent(name, new HashSet<>());
            dependencyMap.get(name).add(modelDependency.getVersion());
        }
        for (Map.Entry<String, Set<String>> entry : dependencyMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                System.out.println("Conflicts dependency: " + entry.getKey());
                conflictNum++;
                for (String v : entry.getValue()) {
                    System.out.println(v);
                }
            }
        }
    }

    private Dependency getDependencyFromTreeLine(String line) {
        String[] split = line.split(" ");
        String[] split1 = split[split.length - 1].split(":");
        return new Dependency(split1[0], split1[1], split1[3]);
    }

    private void doSubModels(String path, Xpp3Dom parentDom) throws Exception {
        for (Xpp3Dom child : parentDom.getChildren()) {
            if (!"modules".equals(child.getName())) {
                continue;
            }
            for (Xpp3Dom model : child.getChildren()) {
                if (!"module".equals(model.getName())) {
                    continue;
                }
                doCheckModel(path + "/" + model.getValue());
            }
        }
    }

    public List<String> loadDependencyTree(String path) throws IOException {
        return IOUtils.readLines(new FileInputStream(path + "/tree.txt"), Charset.defaultCharset());
    }


}
