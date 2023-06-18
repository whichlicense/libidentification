let wasm_bindgen;
(function() {
    const __exports = {};
    let wasm;

    const cachedTextDecoder = new TextDecoder('utf-8', { ignoreBOM: true });

    let cachedUint8Memory0 = null;

    function getUint8Memory0() {
        if (cachedUint8Memory0 === null || cachedUint8Memory0.byteLength === 0) {
            cachedUint8Memory0 = new Uint8Array(wasm.memory.buffer);
        }
        return cachedUint8Memory0;
    }

    function getStringFromWasm0(ptr, len) {
        return cachedTextDecoder.decode(getUint8Memory0().subarray(ptr, ptr + len));
    }

    let WASM_VECTOR_LEN = 0;

    const cachedTextEncoder = new TextEncoder('utf-8');

    const encodeString = (typeof cachedTextEncoder.encodeInto === 'function'
        ? function (arg, view) {
            return cachedTextEncoder.encodeInto(arg, view);
        }
        : function (arg, view) {
            const buf = cachedTextEncoder.encode(arg);
            view.set(buf);
            return {
                read: arg.length,
                written: buf.length
            };
        });

    function passStringToWasm0(arg, malloc, realloc) {

        if (realloc === undefined) {
            const buf = cachedTextEncoder.encode(arg);
            const ptr = malloc(buf.length);
            getUint8Memory0().subarray(ptr, ptr + buf.length).set(buf);
            WASM_VECTOR_LEN = buf.length;
            return ptr;
        }

        let len = arg.length;
        let ptr = malloc(len);

        const mem = getUint8Memory0();

        let offset = 0;

        for (; offset < len; offset++) {
            const code = arg.charCodeAt(offset);
            if (code > 0x7F) break;
            mem[ptr + offset] = code;
        }

        if (offset !== len) {
            if (offset !== 0) {
                arg = arg.slice(offset);
            }
            ptr = realloc(ptr, len, len = offset + arg.length * 3);
            const view = getUint8Memory0().subarray(ptr + offset, ptr + len);
            const ret = encodeString(arg, view);

            offset += ret.written;
        }

        WASM_VECTOR_LEN = offset;
        return ptr;
    }

    function _assertClass(instance, klass) {
        if (!(instance instanceof klass)) {
            throw new Error(`expected instance of ${klass.name}`);
        }
        return instance.ptr;
    }

    let cachedInt32Memory0 = null;

    function getInt32Memory0() {
        if (cachedInt32Memory0 === null || cachedInt32Memory0.byteLength === 0) {
            cachedInt32Memory0 = new Int32Array(wasm.memory.buffer);
        }
        return cachedInt32Memory0;
    }
    /**
     * @param {FuzzyHashingConfig} config
     * @param {string} license
     * @returns {string}
     */
    __exports.fuzzy_compute_hash = function(config, license) {
        try {
            const retptr = wasm.__wbindgen_add_to_stack_pointer(-16);
            _assertClass(config, FuzzyHashingConfig);
            const ptr0 = passStringToWasm0(license, wasm.__wbindgen_malloc, wasm.__wbindgen_realloc);
            const len0 = WASM_VECTOR_LEN;
            wasm.fuzzy_compute_hash(retptr, config.ptr, ptr0, len0);
            var r0 = getInt32Memory0()[retptr / 4];
            var r1 = getInt32Memory0()[retptr / 4 + 1];
            return getStringFromWasm0(r0, r1);
        } finally {
            wasm.__wbindgen_add_to_stack_pointer(16);
            wasm.__wbindgen_free(r0, r1);
        }
    };

    /**
     * @param {FuzzyHashingConfig} config
     * @param {string} license
     * @returns {string}
     */
    __exports.fuzzy_detect_license = function(config, license) {
        try {
            const retptr = wasm.__wbindgen_add_to_stack_pointer(-16);
            _assertClass(config, FuzzyHashingConfig);
            const ptr0 = passStringToWasm0(license, wasm.__wbindgen_malloc, wasm.__wbindgen_realloc);
            const len0 = WASM_VECTOR_LEN;
            wasm.fuzzy_detect_license(retptr, config.ptr, ptr0, len0);
            var r0 = getInt32Memory0()[retptr / 4];
            var r1 = getInt32Memory0()[retptr / 4 + 1];
            return getStringFromWasm0(r0, r1);
        } finally {
            wasm.__wbindgen_add_to_stack_pointer(16);
            wasm.__wbindgen_free(r0, r1);
        }
    };

    /**
     * @param {GaoyaHashingConfig} config
     * @param {string} license
     * @returns {string}
     */
    __exports.gaoya_compute_hash = function(config, license) {
        try {
            const retptr = wasm.__wbindgen_add_to_stack_pointer(-16);
            _assertClass(config, GaoyaHashingConfig);
            const ptr0 = passStringToWasm0(license, wasm.__wbindgen_malloc, wasm.__wbindgen_realloc);
            const len0 = WASM_VECTOR_LEN;
            wasm.gaoya_compute_hash(retptr, config.ptr, ptr0, len0);
            var r0 = getInt32Memory0()[retptr / 4];
            var r1 = getInt32Memory0()[retptr / 4 + 1];
            return getStringFromWasm0(r0, r1);
        } finally {
            wasm.__wbindgen_add_to_stack_pointer(16);
            wasm.__wbindgen_free(r0, r1);
        }
    };

    /**
     * @param {GaoyaHashingConfig} config
     * @param {string} license
     * @returns {string}
     */
    __exports.gaoya_detect_license = function(config, license) {
        try {
            const retptr = wasm.__wbindgen_add_to_stack_pointer(-16);
            _assertClass(config, GaoyaHashingConfig);
            const ptr0 = passStringToWasm0(license, wasm.__wbindgen_malloc, wasm.__wbindgen_realloc);
            const len0 = WASM_VECTOR_LEN;
            wasm.gaoya_detect_license(retptr, config.ptr, ptr0, len0);
            var r0 = getInt32Memory0()[retptr / 4];
            var r1 = getInt32Memory0()[retptr / 4 + 1];
            return getStringFromWasm0(r0, r1);
        } finally {
            wasm.__wbindgen_add_to_stack_pointer(16);
            wasm.__wbindgen_free(r0, r1);
        }
    };

    /**
     */
    class FuzzyHashingConfig {

        static __wrap(ptr) {
            const obj = Object.create(FuzzyHashingConfig.prototype);
            obj.ptr = ptr;

            return obj;
        }

        __destroy_into_raw() {
            const ptr = this.ptr;
            this.ptr = 0;

            return ptr;
        }

        free() {
            const ptr = this.__destroy_into_raw();
            wasm.__wbg_fuzzyhashingconfig_free(ptr);
        }
        /**
         * @param {string} licenses_json
         * @param {number} confidence_threshold
         * @param {boolean} exit_on_exact_match
         */
        constructor(licenses_json, confidence_threshold, exit_on_exact_match) {
            const ptr0 = passStringToWasm0(licenses_json, wasm.__wbindgen_malloc, wasm.__wbindgen_realloc);
            const ret = wasm.fuzzyhashingconfig_new(ptr0, WASM_VECTOR_LEN, confidence_threshold, exit_on_exact_match);
            return FuzzyHashingConfig.__wrap(ret);
        }
    }
    __exports.FuzzyHashingConfig = FuzzyHashingConfig;
    /**
     */
    class GaoyaHashingConfig {

        static __wrap(ptr) {
            const obj = Object.create(GaoyaHashingConfig.prototype);
            obj.ptr = ptr;

            return obj;
        }

        __destroy_into_raw() {
            const ptr = this.ptr;
            this.ptr = 0;

            return ptr;
        }

        free() {
            const ptr = this.__destroy_into_raw();
            wasm.__wbg_gaoyahashingconfig_free(ptr);
        }
        /**
         * @param {string} licenses_json
         * @param {number} band_count
         * @param {number} band_width
         * @param {number} shingle_size
         */
        constructor(licenses_json, band_count, band_width, shingle_size) {
            const ptr0 = passStringToWasm0(licenses_json, wasm.__wbindgen_malloc, wasm.__wbindgen_realloc);
            const ret = wasm.gaoyahashingconfig_new(ptr0, WASM_VECTOR_LEN, band_count, band_width, shingle_size);
            return GaoyaHashingConfig.__wrap(ret);
        }
    }
    __exports.GaoyaHashingConfig = GaoyaHashingConfig;

    function getImports() {
        const imports = {};
        imports.wbg = {};
        imports.wbg.__wbindgen_throw = function(arg0, arg1) {
            throw new Error(getStringFromWasm0(arg0, arg1));
        };

        return imports;
    }

    function finalizeInit(instance, module) {
        wasm = instance.exports;
        cachedInt32Memory0 = null;
        cachedUint8Memory0 = null;
        return wasm;
    }

    function initSync(module) {
        const imports = getImports();

        if (!(module instanceof WebAssembly.Module)) {
            module = new WebAssembly.Module(module);
        }

        const instance = new WebAssembly.Instance(module, imports);

        return finalizeInit(instance, module);
    }

    wasm_bindgen = Object.assign({ initSync }, __exports);
})();
