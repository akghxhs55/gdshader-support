package kr.jaehoyi.gdshader.formatter

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdsFormatterTest : BasePlatformTestCase() {
    fun `test global definitions`() {
        doTest(
            """
            shader_type canvas_item;
            render_mode unshaded,blend_disabled;
            
            uniform float height;
            const float PI=3.14159;
            varying vec3 v_normal;
            """.trimIndent(),
            """
            shader_type canvas_item;
            render_mode unshaded, blend_disabled;
            
            uniform float height;
            const float PI = 3.14159;
            varying vec3 v_normal;
            """.trimIndent(),
        )
    }

    fun `test function declaration`() {
        doTest(
            """
            void fragment(  ){
            ALBEDO=vec3(1.0,0.0,0.0);
            }
            """.trimIndent(),
            """
            void fragment() {
            	ALBEDO = vec3(1.0, 0.0, 0.0);
            }
            """.trimIndent(),
        )
    }

    fun `test struct declaration`() {
        doTest(
            """
            struct Light{
            vec3 position;
            vec3 color;
            float intensity;
            };
            """.trimIndent(),
            """
            struct Light {
            	vec3 position;
            	vec3 color;
            	float intensity;
            };
            """.trimIndent(),
        )
    }

    fun `test control flow`() {
        doTest(
            """
            void test() {
            if(true){
            do_something();
            }else{
            do_other();
            }
            
            for(int i=0;i<10;i++){
            loop_body();
            }
            
            while(condition){
            keep_going();
            }
            }
            """.trimIndent(),
            """
            void test() {
            	if (true) {
            		do_something();
            	} else {
            		do_other();
            	}

            	for (int i = 0; i < 10; i++) {
            		loop_body();
            	}

            	while (condition) {
            		keep_going();
            	}
            }
            """.trimIndent(),
        )
    }

    fun `test switch statement`() {
        doTest(
            """
            void test() {
            switch(value){
            case 0:
            handle_zero();
            break;
            case 1:
            {
            handle_one();
            }
            break;
            default:
            handle_default();
            }
            }
            """.trimIndent(),
            """
            void test() {
            	switch (value) {
            		case 0:
            			handle_zero();
            			break;
            		case 1:
            			{
            				handle_one();
            			}
            			break;
            		default:
            			handle_default();
            	}
            }
            """.trimIndent(),
        )
    }

    fun `test expressions spacing`() {
        doTest(
            """
            void test() {
            float a=1.0+2.0*3.0;
            bool b=(x>y)&&(z<=w);
            vec3 v=vec3(a,b,c);
            a+=1.0;
            }
            """.trimIndent(),
            """
            void test() {
            	float a = 1.0 + 2.0 * 3.0;
            	bool b = (x > y) && (z <= w);
            	vec3 v = vec3(a, b, c);
            	a += 1.0;
            }
            """.trimIndent(),
        )
    }

    fun `test comments indentation`() {
        doTest(
            """
            void test() {
            // Line comment
            if(true) {
            /* Block
            Comment */
            do_something();
            }
            }
            """.trimIndent(),
            """
            void test() {
            	// Line comment
            	if (true) {
            		/* Block
            		Comment */
            		do_something();
            	}
            }
            """.trimIndent(),
        )
    }

    fun `test complex formatting`() {
        doTest(
            """
            uniform float val:hint_range(0,1)=0.5;
            uniform vec4 col:source_color,filter_linear;
            
            struct S {
            float x;
            };
            
            void test() {
            float a=1.0,b=2.0,c;
            S o;
            o.n.x = (a>b)?a:b;
            for(int i=0;i<3;i++){
            o.arr[i]=float(i)*1.5;
            if(o.arr[i]>2.0){
            do_something();
            } else {
            /*
             * multi-line
             * comment
             */
            do_other();
            }
            }
            }
            """.trimIndent(),
            """
            uniform float val : hint_range(0, 1) = 0.5;
            uniform vec4 col : source_color, filter_linear;

            struct S {
            	float x;
            };

            void test() {
            	float a = 1.0, b = 2.0, c;
            	S o;
            	o.n.x = (a > b) ? a : b;
            	for (int i = 0; i < 3; i++) {
            		o.arr[i] = float(i) * 1.5;
            		if (o.arr[i] > 2.0) {
            			do_something();
            		} else {
            			/*
            			 * multi-line
            			 * comment
            			 */
            			do_other();
            		}
            	}
            }
            """.trimIndent(),
        )
    }

    fun `test empty blocks and single statements`() {
        doTest(
            """
            void empty_block() {}
            void single_statement() {if(true) return;}
            """.trimIndent(),
            """
            void empty_block() { }
            void single_statement() { if (true) return; }
            """.trimIndent(),
        )
    }

    private fun doTest(
        code: String,
        expected: String,
    ) {
        myFixture.configureByText("test.gdshader", code)
        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(myFixture.file)
        }
        myFixture.checkResult(expected)
    }
}
