/**
 * @author TianLong Liu
 * @date 2026-03-24 14:28:18
 * @description
 */

package org.will.demo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TianLong Liu
 * @date 2026-03-24 14:28:18
 */
@RestController
@RequestMapping
public class IndexController {

    @GetMapping
    public String index() {
        return "Hello World!";
    }
}
