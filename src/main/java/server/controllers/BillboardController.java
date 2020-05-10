package server.controllers;

import common.router.*;
import server.router.*;
import server.sql.CollectionFactory;

import java.util.List;

public class BillboardController {

    public class Get extends Action {
        public Get() {}

        @Override
        public IActionResult execute(Request req) throws Exception {

            List<common.models.Billboard> res = CollectionFactory.getInstance(common.models.Billboard.class).get(x -> true);

            return new Ok(res);
        }
    }

    public class GetById extends Action {
        public GetById() {}

        @Override
        public IActionResult execute(Request req) throws Exception {
            String id = req.params.get("id");

            List<common.models.Billboard> res = CollectionFactory.getInstance(common.models.Billboard.class).get(x -> id == String.valueOf(x.id));

            return new Ok(res);
        }
    }

    public class GetByLock extends Action {
        public GetByLock() {}

        @Override
        public IActionResult execute(Request req) throws Exception {
            String lock = req.params.get("lock");

            List<common.models.Billboard> res = CollectionFactory.getInstance(common.models.Billboard.class).get(x -> String.valueOf(x.locked) == lock);

            return new Ok(res);
        }
    }

    public class Insert extends Action {
        public Insert() {}

        @Override
        public IActionResult execute(Request req) throws Exception {

            if (req.body instanceof common.models.Billboard) {
                CollectionFactory.getInstance(common.models.Billboard.class).insert((common.models.Billboard) req.body);
                return new Ok();
            }

            return new BadRequest("Not a billboard");
        }
    }

    public class Update extends Action {
        public Update() {}

        @Override
        public IActionResult execute(Request req) throws Exception {
            if (req.body instanceof common.models.Billboard) {
                CollectionFactory.getInstance(common.models.Billboard.class).update((common.models.Billboard) req.body);
                return new Ok();
            }

            return new BadRequest("Not a billboard");
        }
    }

    public class Delete extends Action {
        public Delete() {}

        @Override
        public IActionResult execute(Request req) throws Exception {
            if (req.body instanceof common.models.Billboard) {
                CollectionFactory.getInstance(common.models.Billboard.class).delete((common.models.Billboard) req.body);
                return new Ok();
            }

            return new BadRequest("Not a billboard");
        }
    }


}

