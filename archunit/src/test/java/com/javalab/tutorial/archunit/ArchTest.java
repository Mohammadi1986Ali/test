package com.javalab.tutorial.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArchTest {

    final private JavaClasses classes = new ClassFileImporter().importPackages("com.javalab.tutorial.archunit");

    @Test
    public void givenWebLayerClasses_whenCheckingNoDependencyWithService_thenShouldThrowException() {
        ArchRule archRule = noClasses()
                .that()
                .resideInAPackage("..web..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..service..");
        assertThrows(AssertionError.class, () -> archRule.check(classes));
    }

    @Test
    public void givenWebLayerClasses_whenCheckingDependencyWithOtherLayers_thenShouldCheckOk() {
        ArchRule archRule = classes()
                .that()
                .resideInAPackage("..web..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage("..service..", "..api..", "java..");
        archRule.check(classes);
    }

    @Test
    public void givenApplicationClasses_thenNoLayerViolationsShouldExist() {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controllers").definedBy("com.javalab.tutorial.archunit.web..")
                .layer("Services").definedBy("com.javalab.tutorial.archunit.service..")
                .layer("Persistence").definedBy("com.javalab.tutorial.archunit.repository..")
                .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
                .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers")
                .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services")
                .check(classes);
    }
}
