## README

#### About

`tape.tape.guis7`

7GUIs implementation to showcase Tape Framework.
See https://eugenkiss.github.io/7guis/

#### Usage

```bash
cd 7guis
CLJ_CONFIG=../versions/ clj -A:versions:test
```

```clojure
(build/big) ;; figwheel with piggieback
;; Opens http://localhost:9500/
```

```clojure
(in-ns 'tape.guis7.dev)
```

#### License

Copyright © 2020 clyfe

Distributed under the MIT license.
