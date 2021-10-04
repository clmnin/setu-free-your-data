import logging
import os
import sys

from loguru import logger
from uvicorn import Config, Server

from app.server.custom_logger import JSON_LOGS, LOG_LEVEL, InterceptHandler


def setup_logging():
    # intercept everything at the root logger
    logging.root.handlers = [InterceptHandler()]
    logging.root.setLevel(LOG_LEVEL)

    # remove every other logger's handlers
    # and propagate to root logger
    for name in logging.root.manager.loggerDict.keys():
        logging.getLogger(name).handlers = []
        logging.getLogger(name).propagate = True

    # configure loguru
    logger.configure(handlers=[{"sink": sys.stdout, "serialize": JSON_LOGS}])


if __name__ == "__main__":
    server = Server(
        Config(
            "app.main:app", host="0.0.0.0", log_level=LOG_LEVEL, port=int(8000), workers=1
        ),
    )

    # setup logging last, to make sure no library overwrites it
    # (they shouldn't, but it happens)
    setup_logging()

    server.run()
