package fractalutils.resource

import fractalutils.resource.classpath.JarResourceSystem
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ResourceTest {

    @Test
    fun testResourceLoading() {
        val resourceSystem = JarResourceSystem()
        assertEquals("asdf", resourceSystem["test.txt"].readText(), "Resources should load from root")
    }

    @Test
    fun testResourceLoadingWithRootPath() {
        val resourceSystem = JarResourceSystem("resourceLoadingTest")
        assertEquals( "2", resourceSystem["b.txt"].readText(), "Resources should load")
        assertEquals( "abc", resourceSystem["subDir/test"].readText(), "Subdirectories should work")
    }

    @Test
    fun testIncludes() {
        val resourceSystem = JarResourceSystem("resourceLoadingTest")
        assertEquals( "1\n2\n3\n4\n5\n6\n7\n", resourceSystem["a.txt"].readTextWithIncludes(), "Includes should work")
    }

    @Test
    fun testNamesAndSuch() {
        val resourceSystem = JarResourceSystem("resourceLoadingTest")
        val a = resourceSystem["a.txt"]
        val d = resourceSystem["subDir/d.txt"]
        val t = resourceSystem["subDir/test"]

        assertEquals( "a.txt", a.path, "path should be correct for root")
        assertEquals( "subDir/d.txt", d.path, "path should be correct for subdirs",)
        assertEquals( "", a.parentPath, "dir should be correct for root")
        assertEquals( "subDir", d.parentPath, "dir should be correct for subdirs")
        assertEquals( "a.txt", a.name, "name should be correct for root")
        assertEquals( "d.txt", d.name, "name should be correct for subdirs")
        assertEquals("test", t.name, "name should be correct for extensionless names")
        assertEquals("txt", d.extension, "extension should be correct")
        assertEquals("", t.extension, "extension should be correct for extensionless names")
        assertEquals("d", d.nameWithoutExtension, "name without extension should be correct")
        assertEquals("test", t.nameWithoutExtension, "name without extension should be correct for extensionless names")
    }

    @Test
    fun testExists() {
        val resourceSystem = JarResourceSystem("resourceLoadingTest")
        val a = resourceSystem["a.txt"]
        val z = resourceSystem["z.txt"]

        assertTrue(a.exists, "Existing resources should return true for exists")
        assertFalse(z.exists, "Non-existing resources should return false for exists")

        assertTrue(a.isFile, "Existing resources should return true for isFile")
        assertFalse(z.isFile, "Non-existing resources should return false for isFile")

    }


    @Test
    fun testLineMapping() {
        val resourceSystem = JarResourceSystem("resourceLoadingTest")
        val lineMapping = SourceLineMapping<String>()
        resourceSystem["a.txt"].readTextWithIncludes(sourceMappingOut = lineMapping)

        assertEquals(Pair("a.txt", 1), lineMapping.getSourceAndLine(1), "Source lookup should work")
        assertEquals(Pair("b.txt", 1), lineMapping.getSourceAndLine(2), "Source lookup should work")
        assertEquals(Pair("a.txt", 3), lineMapping.getSourceAndLine(3), "Source lookup should work")
        assertEquals(Pair("subDir/c.txt", 1), lineMapping.getSourceAndLine(4), "Source lookup should work")
        assertEquals(Pair("subDir/d.txt", 1), lineMapping.getSourceAndLine(5), "Source lookup should work")
        assertEquals(Pair("subDir/c.txt", 3), lineMapping.getSourceAndLine(6), "Source lookup should work")
        assertEquals(Pair("a.txt", 6), lineMapping.getSourceAndLine(7), "Source lookup should work")


    }
}